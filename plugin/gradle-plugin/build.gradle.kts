/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.gradle.maven-publish")
    id("kotlin-kapt")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin")
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    compileOnly(libs.kotlinGradlePlugin)
    implementation(libs.kotlinGradlePluginApi)

    compileOnly(libs.autoService)
    kapt(libs.autoService)
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "compilerPluginVersion", value = project.version.toString())
    }
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            from(components["java"])
        }
    }
}
