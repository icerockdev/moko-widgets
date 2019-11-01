/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.CollectAdditionalSourcesExtension
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.stubs.KotlinAnnotationEntryStub
import org.jetbrains.kotlin.psi.stubs.KotlinClassStub
import org.jetbrains.kotlin.psi.stubs.KotlinFileStub
import org.jetbrains.kotlin.psi.stubs.KotlinImportDirectiveStub
import org.jetbrains.kotlin.psi.stubs.KotlinModifierListStub
import org.jetbrains.kotlin.psi.stubs.KotlinParameterStub
import org.jetbrains.kotlin.psi.stubs.KotlinPlaceHolderStub
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes
import java.io.File

class GenerateSourcesExtension : CollectAdditionalSourcesExtension {

    data class GeneratorInput(
        val packageName: String,
        val className: String,
        val imports: List<String>,
        val arguments: Map<String, String>
    )

    fun KotlinFileStub.processFileStub(): List<GeneratorInput> {
        val importList = childrenStubs.firstOrNull { it.stubType == KtStubElementTypes.IMPORT_LIST }
        val imports = importList?.childrenStubs
            .orEmpty()
            .filterIsInstance<KotlinImportDirectiveStub>()
            .map { it.getImportedFqName() }
            .mapNotNull { it?.asString() }

        return childrenStubs.filterIsInstance<KotlinClassStub>().mapNotNull { classStub ->
            val modifiersList =
                classStub.childrenStubs.filterIsInstance<KotlinModifierListStub>().firstOrNull()
                    ?: return@mapNotNull null
            val annotations = modifiersList.childrenStubs.filterIsInstance<KotlinAnnotationEntryStub>()
            val containWidgetDefAnnotation = annotations.any { it.getShortName() == "WidgetDef" }

            if (!containWidgetDefAnnotation) return@mapNotNull null

            val constructor =
                classStub.childrenStubs.firstOrNull { it.stubType == KtStubElementTypes.PRIMARY_CONSTRUCTOR }
                    ?: return@mapNotNull null
            val constructorParameters =
                constructor.childrenStubs.firstOrNull { it.stubType == KtStubElementTypes.VALUE_PARAMETER_LIST }
                    ?: return@mapNotNull null

            val params = constructorParameters
                .childrenStubs
                .filterIsInstance<KotlinParameterStub>()
                .associate { parameterStub ->
                    val parameterName = parameterStub.name

                    val type = parameterStub.childrenStubs
                        .mapNotNull { it as? KotlinPlaceHolderStub<KtTypeReference> }
                        .mapNotNull { typeReferenceStub ->
                            val parameterType = typeReferenceStub.psi.text
                            parameterType
                        }.first()

                    parameterName!! to type
                }

            GeneratorInput(
                packageName = this.getPackageFqName().asString(),
                className = classStub.name!!,
                imports = imports,
                arguments = params
            )
        }
    }

    override fun collectAdditionalSourcesAndUpdateConfiguration(
        knownSources: Collection<KtFile>,
        configuration: CompilerConfiguration,
        project: Project
    ): Collection<KtFile> {
        val toGeneration = knownSources.mapNotNull { file ->
            val stubTree = file.calcStubTree()
            val stub = stubTree.root as? KotlinFileStub

            stub?.processFileStub()
        }.flatten()

        val psiManager = PsiManager.getInstance(project)
        val localFileSystem = VirtualFileManager.getInstance().getFileSystem(StandardFileSystems.FILE_PROTOCOL)

        val generated = toGeneration.map { input ->
            // TODO pass from gradle-plugin
            val projectRoot =
                "/Users/alekseymikhailovwork/Documents/development/icerockdev_workspace/moko/moko-widgets/widgets"
            val dir = File("$projectRoot/build/generated/mokoWidgets")
            val file = File(dir, "${input.className}Gen.kt")

            val importNames = input.imports.plus("dev.icerock.moko.widgets.core.WidgetScope").distinct()

            val imports = importNames.joinToString("\n") { "import $it" }
            val widgetName = input.className
            val shortName = widgetName.replace("Widget", "").decapitalize()
            val customArgs = input.arguments.filter {
                when (it.key) {
                    "factory", "style" -> false
                    else -> true
                }
            }

            fun isIdType(string: String) = string == "Id?" || string == "Id"

            val params = customArgs.map {
                val type = if (isIdType(it.value)) {
                    "$widgetName.${it.value}"
                } else {
                    it.value
                }
                val def = if (type.endsWith("?")) {
                    " = null"
                } else {
                    ""
                }
                "    ${it.key}: $type$def"
            }.joinToString(",\n")
            val paramsSet = customArgs.map {
                "    ${it.key} = ${it.key}"
            }.joinToString(",\n")
            val shortNameUpper = shortName.capitalize()
            val haveIdArg = customArgs.any { isIdType(it.value) }

            val coreContent = """package ${input.packageName}

$imports

private object ${widgetName}FactoryKey: WidgetScope.Key<VFC<$widgetName>>
private object ${widgetName}StyleKey: WidgetScope.Key<$widgetName.Style>

val WidgetScope.${shortName}Factory: VFC<$widgetName>
        by WidgetScope.readProperty(${widgetName}FactoryKey, ::${shortName}WidgetViewFactory)

var WidgetScope.Builder.${shortName}Factory: VFC<$widgetName>
        by WidgetScope.readWriteProperty(${widgetName}FactoryKey, WidgetScope::${shortName}Factory)

val WidgetScope.${shortName}Style: $widgetName.Style
        by WidgetScope.readProperty(${widgetName}StyleKey) { $widgetName.Style() }

var WidgetScope.Builder.${shortName}Style: $widgetName.Style
        by WidgetScope.readWriteProperty(${widgetName}StyleKey, WidgetScope::${shortName}Style)
"""

            val idContent = """

fun WidgetScope.$shortName(
    factory: VFC<$widgetName>,
    style: $widgetName.Style,
$params
) = $widgetName(
    factory = factory,
    style = style,
$paramsSet
)

fun WidgetScope.get${shortNameUpper}Factory(id: $widgetName.Id): VFC<$widgetName> {
    return getIdProperty(id, ${widgetName}FactoryKey, ::${shortName}Factory)
}

fun WidgetScope.Builder.set${shortNameUpper}Factory(factory: VFC<$widgetName>, vararg ids: $widgetName.Id) {
    ids.forEach { setIdProperty(it, ${widgetName}FactoryKey, factory) }
}

fun WidgetScope.get${shortNameUpper}Style(id: $widgetName.Id): $widgetName.Style {
    return getIdProperty(id, ${widgetName}StyleKey, ::${shortName}Style)
}

fun WidgetScope.Builder.set${shortNameUpper}Style(style: $widgetName.Style, vararg ids: $widgetName.Id) {
    ids.forEach { setIdProperty(it, ${widgetName}StyleKey, style) }
}

fun WidgetScope.$shortName(
$params
) = $widgetName(
    factory = id?.let { this.get${shortNameUpper}Factory(it) } ?: this.${shortName}Factory,
    style = id?.let { this.get${shortNameUpper}Style(it) } ?: this.${shortName}Style,
$paramsSet
)

fun WidgetScope.$shortName(
    styled: ($widgetName.Style) -> $widgetName.Style,
$params
) = $widgetName(
    factory = id?.let { this.get${shortNameUpper}Factory(it) } ?: this.${shortName}Factory,
    style = styled(id?.let { this.get${shortNameUpper}Style(it) } ?: this.${shortName}Style),
$paramsSet
)"""

            val noIdContent = """

fun WidgetScope.$shortName(
    factory: VFC<$widgetName> = this.${shortName}Factory,
    style: $widgetName.Style = this.${shortName}Style,
$params
) = $widgetName(
    factory = factory,
    style = style,
$paramsSet
)
"""

            val content = if (haveIdArg) {
                coreContent + idContent
            } else {
                coreContent + noIdContent
            }

            dir.mkdirs()
            file.writeText(content)

            val virtualFile = localFileSystem.findFileByPath(file.path)!!
            psiManager.findFile(virtualFile) as KtFile
        }

        return knownSources + generated
    }
}