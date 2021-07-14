/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.5.20")
    id("detekt-convention")
    id("publication-convention")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

buildscript {
    repositories {
        mavenCentral()
        google()

        gradlePluginPortal()
        jcenter()
    }
    dependencies {
        classpath("dev.icerock:mobile-multiplatform:0.12.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")
    }
}

allprojects {
    group = "dev.icerock.moko"
    version = rootProject.libs.versions.mokoWidgetsVersion.get()

    project.plugins.withType<JavaPlugin> {
        project.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}
