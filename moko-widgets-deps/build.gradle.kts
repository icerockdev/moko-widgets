/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.4.30-M1")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

repositories {
    jcenter()
    google()

    maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
}

dependencies {
    api(gradleApi())
    api("dev.icerock:mobile-multiplatform:0.9.0")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30-M1")
    api("com.android.tools.build:gradle:4.1.1")
}

group = "gradle"
version = "1"

val mokoWidgetsVersion: String = when (properties.containsKey("devPublish")) {
    true -> getGitCommit()
    false -> "0.1.0-dev-21"
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "widgetsVersion", value = mokoWidgetsVersion)
    }
}

fun getGitCommit(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
