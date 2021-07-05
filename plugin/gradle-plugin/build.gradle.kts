/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("kotlin-kapt")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

dependencies {
    implementation(libs.kotlinStdLib)

    compileOnly(libs.kotlinGradlePlugin)
    implementation(libs.kotlinGradlePluginApi)

    compileOnly(Deps.Libs.Jvm.autoService)
    kapt(Deps.Libs.Jvm.autoService)
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "compilerPluginVersion", value = project.version.toString())
    }
}
