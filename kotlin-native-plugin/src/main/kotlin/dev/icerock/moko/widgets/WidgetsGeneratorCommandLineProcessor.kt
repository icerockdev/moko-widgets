/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

object GenerationDir : CompilerConfigurationKey<String>("generationDir")

@AutoService(CommandLineProcessor::class)
class WidgetsGeneratorCommandLineProcessor : CommandLineProcessor {
    override val pluginId = CommonCommandLineProcessor.pluginId
    override val pluginOptions = listOf(
        CliOption(CommonCommandLineProcessor.generationDirOptionName, "valdesc", "desc")
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            CommonCommandLineProcessor.generationDirOptionName -> configuration.put(GenerationDir, value)
        }
    }
}
