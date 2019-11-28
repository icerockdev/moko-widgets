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
        val widgetName = input.className

        val importNames = input.imports
            .plus("dev.icerock.moko.widgets.core.Theme")
            .plus("dev.icerock.moko.widgets.factory.Default${widgetName}ViewFactory")
            .distinct()

        val imports = importNames.joinToString("\n") { "import $it" }
        val shortName = widgetName.replace("Widget", "").decapitalize()
        val customArgs = input.arguments.filter {
            when (it.key) {
                "factory" -> false
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

private object ${widgetName}FactoryKey: Theme.Key<ViewFactory<$widgetName<out WidgetSize>>>

val Theme.${shortName}Factory: ViewFactory<$widgetName<out WidgetSize>>
        by Theme.readProperty(${widgetName}FactoryKey) { Default${widgetName}ViewFactory() }

var Theme.Builder.${shortName}Factory: ViewFactory<$widgetName<out WidgetSize>>
        by Theme.readWriteProperty(${widgetName}FactoryKey, Theme::${shortName}Factory)

"""

        val idContent = """

fun <WS: WidgetSize> Theme.$shortName(
    factory: ViewFactory<$widgetName<out WidgetSize>>,
$params
) = $widgetName(
    factory = factory,
$paramsSet
)

fun Theme.get${shortNameUpper}Factory(id: $widgetName.Id): ViewFactory<$widgetName<out WidgetSize>> {
    return getIdProperty(id, ${widgetName}FactoryKey, ::${shortName}Factory)
}

fun Theme.Builder.set${shortNameUpper}Factory(
    factory: ViewFactory<$widgetName<out WidgetSize>>, 
    vararg ids: $widgetName.Id
) {
    ids.forEach { setIdProperty(it, ${widgetName}FactoryKey, factory) }
}

fun <WS: WidgetSize> Theme.$shortName(
$params
) = $widgetName(
    factory = id?.let { this.get${shortNameUpper}Factory(it) } ?: this.${shortName}Factory,
$paramsSet
)"""

        val noIdContent = """

fun <WS: WidgetSize> Theme.$shortName(
    factory: ViewFactory<$widgetName<out WidgetSize>> = this.${shortName}Factory,
$params
) = $widgetName(
    factory = factory,
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