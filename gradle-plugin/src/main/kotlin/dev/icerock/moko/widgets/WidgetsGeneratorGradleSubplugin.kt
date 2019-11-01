/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@AutoService(KotlinGradleSubplugin::class) // don't forget!
class WidgetsGeneratorGradleSubplugin : KotlinGradleSubplugin<AbstractCompile> {
    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> {
        return emptyList()
    }

    override fun isApplicable(project: Project, task: AbstractCompile) =
        project.plugins.hasPlugin(WidgetsGeneratorGradlePlugin::class.java)

    override fun getCompilerPluginId(): String = "widgets-generator"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "dev.icerock.moko.widgets",
        artifactId = "kotlin-plugin",
        version = "0.1.0" // remember to bump this version before any release!
    )

    override fun getNativeCompilerPluginArtifact(): SubpluginArtifact? = SubpluginArtifact(
        groupId = "dev.icerock.moko.widgets",
        artifactId = "kotlin-native-plugin",
        version = "0.1.0" // remember to bump this version before any release!
    )
}
