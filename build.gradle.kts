/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()

        jcenter()
    }
    dependencies {

        classpath("com.google.gms:google-services:4.3.8")
        classpath("dev.icerock.moko.widgets:gradle-plugin:0.1.0-dev-20")
        classpath("dev.icerock.moko:resources-generator:0.16.1")
        classpath("dev.icerock:mobile-multiplatform:0.12.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
        classpath("com.android.tools.build:gradle:4.2.1")

        classpath(":widgets-build-logic")
    }
}

allprojects {
    // Workaround for https://youtrack.jetbrains.com/issue/KT-36721.
    pluginManager.withPlugin("kotlin-multiplatform") {
        val kotlinExtension = project.extensions.getByName("kotlin")
                as org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
        val uniqueName = "${project.group}.${project.name}"

        kotlinExtension.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
            compilations["main"].kotlinOptions.freeCompilerArgs += listOf(
                "-module-name",
                uniqueName
            )
        }
    }

    configurations
        .matching { it.name == "compileOnly" }
        .configureEach {
            dependencies {
                // fix of package javax.annotation does not exist import javax.annotation.Generated in DataBinding code
                "compileOnly"("javax.annotation:jsr250-api:1.0")
            }
        }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
