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

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.5.20")

    compileOnly(libs.autoService)
    kapt(libs.autoService)
}


buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "compilerPluginVersion", value = project.version.toString())
    }
}
