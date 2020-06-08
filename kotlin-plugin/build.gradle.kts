/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("kotlin")
    id("maven-publish")
    id("kotlin-kapt")
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

dependencies {
    implementation(Deps.Libs.Jvm.kotlinStdLib)

    compile(project(":kotlin-common-plugin"))

    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    compileOnly(Deps.Libs.Jvm.autoService)
    kapt(Deps.Libs.Jvm.autoService)
}

val shadowJar: ShadowJar by tasks
shadowJar.apply {
    classifier = ""
    configurations = listOf(project.configurations.compile.get())
}

publishing {
    publications {
        register("pluginMaven", MavenPublication::class) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            artifact(shadowJar)
        }
    }
}
