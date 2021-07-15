/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.gradle.maven-publish")
    id("kotlin-kapt")
    id("com.github.gmazzo.buildconfig")
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    compileOnly(libs.kotlinGradlePlugin)
    implementation(libs.kotlinGradlePluginApi)

    compileOnly(libs.autoService)
    kapt(libs.autoService)
}

buildConfig {
    sourceSets.getByName("main") {
        buildConfig {
            buildConfigField("String", "compilerPluginVersion", "\"${project.version}\"")
        }
    }
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            from(components["java"])
        }
    }
}
