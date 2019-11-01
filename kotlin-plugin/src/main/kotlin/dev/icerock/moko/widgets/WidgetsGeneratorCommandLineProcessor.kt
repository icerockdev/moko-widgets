/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(CommandLineProcessor::class)
class WidgetsGeneratorCommandLineProcessor : CommandLineProcessor {
    override val pluginId = "widgets-generator"
    override val pluginOptions = emptyList<AbstractCliOption>()

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        println("option $option value $value conf $configuration")
    }
}
