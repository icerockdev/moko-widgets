/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import BuildConfig
import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@AutoService(KotlinGradleSubplugin::class)
class WidgetsGeneratorGradleSubplugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> {
        return listOf(
            SubpluginOption("generationDir", getGenerationDir(project).path)
        )
    }

    override fun isApplicable(project: Project, task: AbstractCompile): Boolean {
        return project.plugins.hasPlugin(WidgetsGeneratorGradlePlugin::class.java)
    }

    override fun getCompilerPluginId(): String = "widgets-generator"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "dev.icerock.moko.widgets",
        artifactId = "kotlin-plugin",
        version = BuildConfig.compilerPluginVersion
    )

    override fun getNativeCompilerPluginArtifact(): SubpluginArtifact? = SubpluginArtifact(
        groupId = "dev.icerock.moko.widgets",
        artifactId = "kotlin-native-plugin",
        version = BuildConfig.compilerPluginVersion
    )
}
