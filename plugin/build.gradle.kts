/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.5.20")
    id("detekt-convention")
    id("publication-convention")
}

group = "dev.icerock.moko"
version = libs.versions.mokoWidgetsVersion.get()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
}

publishing.publications.register("mavenJava", MavenPublication::class) {
    from(components["java"])
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}
