/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import java.io.File

abstract class CommonGenerator<KtFile, KtFileStub> {
    data class GeneratorInput(
        val packageName: String,
        val className: String,
        val imports: List<String>,
        val arguments: Map<String, ArgInfo>
    ) {
        data class ArgInfo(
            val type: String,
            val defaultValue: String?
        )
    }

    abstract fun getStub(file: KtFile): KtFileStub?
    abstract fun getGeneratorInput(ktFileStub: KtFileStub): List<GeneratorInput>
    abstract fun getKtFileFromFile(file: File): KtFile
    abstract fun log(string: String)

    fun collectAdditionalSourcesAndUpdateConfiguration(
        knownSources: Collection<KtFile>,
        generationDir: String
    ): Collection<KtFile> {
        val toGeneration = knownSources.mapNotNull { file ->
            val stub = getStub(file) ?: return@mapNotNull null
            getGeneratorInput(stub)
        }.flatten()

        log("to generation selected ${toGeneration.size} classes")

        val generated = toGeneration.map { input ->
            val dir = File(generationDir)
            val file = File(dir, "${input.className}Gen.kt")

            val content = generateFileContent(input)

            dir.mkdirs()
            file.writeText(content)

            log("write to file $file")

            getKtFileFromFile(file)
        }

        return knownSources + generated
    }

    private fun generateFileContent(input: GeneratorInput): String {
        val importNames = input.imports
            .plus("dev.icerock.moko.widgets.core.WidgetScope")
            .distinct()

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

        val params = customArgs.map { (name, info) ->
            val type = if (isIdType(info.type)) {
                "$widgetName.${info.type}"
            } else {
                info.type
            }
            val def = when {
                info.defaultValue != null -> " = ${info.defaultValue}"
                type.endsWith("?") -> " = null"
                else -> ""
            }
            "    $name: $type$def"
        }.joinToString(",\n")
        val paramsSet = customArgs.map {
            "    ${it.key} = ${it.key}"
        }.joinToString(",\n")
        val shortNameUpper = shortName.capitalize()
        val haveIdArg = customArgs.any { isIdType(it.value.type) }

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

        return if (haveIdArg) {
            coreContent + idContent
        } else {
            coreContent + noIdContent
        }
    }
}