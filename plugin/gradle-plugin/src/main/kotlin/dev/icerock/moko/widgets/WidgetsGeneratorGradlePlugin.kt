/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.gradle_plugin.BuildConfig
import org.gradle.api.Project
import org.gradle.api.internal.provider.DefaultProvider
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import java.io.File

open class WidgetsGeneratorGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {
        target.plugins.withType<KotlinMultiplatformPluginWrapper> {
            target.configure<KotlinMultiplatformExtension> {
                val path = getGenerationDir(target).path
                sourceSets.getByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME).kotlin.srcDir(path)
            }
        }
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return DefaultProvider {
            listOf(
                SubpluginOption(
                    key = "generationDir",
                    value = getGenerationDir(kotlinCompilation.target.project).path
                )
            )
        }
    }

    override fun getCompilerPluginId(): String = "widgets-generator"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "dev.icerock.moko.widgets",
        artifactId = "kotlin-plugin",
        version = BuildConfig.compilerPluginVersion
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }
}

fun getGenerationDir(project: Project): File {
    return File(project.buildDir, "generated/mokoWidgets")
}
