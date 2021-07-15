/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.5.20")
    id("detekt-convention")
    id("publication-convention")
    id("com.github.gmazzo.buildconfig") version ("3.0.2")
}

buildscript {
    repositories {
        mavenCentral()
        google()

        gradlePluginPortal()
        jcenter()
    }
    dependencies {
        classpath(":widgets-build-logic")
    }
}

allprojects {
    group = "dev.icerock.moko.widgets"
    version = rootProject.libs.versions.mokoWidgetsVersion.get()

    project.plugins.withType<JavaPlugin> {
        project.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}
publishing.publications.register("mavenJava", MavenPublication::class) {
    from(components["java"])
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}
