/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.3.71"
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

repositories {
    jcenter()
    google()

    maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
}

val devPublishing: Boolean = properties.containsKey("devPublish")
val mokoWidgetsVersion: String = when (devPublishing) {
    true -> getGitCommit()
    false -> "0.1.0-dev-20"
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "widgetsVersion", value = mokoWidgetsVersion)
    }
}

dependencies {
    implementation("dev.icerock:mobile-multiplatform:0.6.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
    implementation("com.android.tools.build:gradle:3.6.1")
}

fun getGitCommit(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
