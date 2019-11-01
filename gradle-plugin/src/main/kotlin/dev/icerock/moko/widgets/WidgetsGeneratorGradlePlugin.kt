/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

open class WidgetsGeneratorGradlePlugin : org.gradle.api.Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            val mpp = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
            val genDir = File(project.buildDir, "generated/mokoWidgets")
            mpp.sourceSets.getByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME).kotlin.srcDir(genDir.path)
        }
    }
}
