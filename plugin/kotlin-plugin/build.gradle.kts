/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.gradle.maven-publish")
    id("kotlin-kapt")
    id("embed-configuration-convention")
}

dependencies {
    "embedImplementation"(projects.kotlinCommonPlugin)

    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    compileOnly(libs.autoService)
    kapt(libs.autoService)
}

publishing {
    publications {
        register("pluginMaven", MavenPublication::class) {
            from(components["java"])
        }
    }
}
