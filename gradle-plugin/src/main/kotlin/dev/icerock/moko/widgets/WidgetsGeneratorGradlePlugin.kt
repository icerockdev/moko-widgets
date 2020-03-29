/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMultiplatformPlugin
import java.io.File

open class WidgetsGeneratorGradlePlugin : org.gradle.api.Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType<KotlinMultiplatformPlugin> {
            project.configure<KotlinMultiplatformExtension> {
                val path = getGenerationDir(project).path
                sourceSets.getByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME).kotlin.srcDir(path)
            }
        }
    }
}

fun getGenerationDir(project: Project): File {
    return File(project.buildDir, "generated/mokoWidgets")
}
