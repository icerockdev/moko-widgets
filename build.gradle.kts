/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        jcenter()
    }
    dependencies {
        classpath("dev.icerock.moko:resources-generator:0.16.2")
        classpath("dev.icerock.moko.widgets:gradle-plugin")
        
        classpath(":widgets-build-logic")
    }
}

allprojects {

    plugins.withId("org.gradle.maven-publish") {
        group = "dev.icerock.moko"
        version = libs.versions.mokoWidgetsVersion.get()
    }

    configurations
        .matching { it.name == "compileOnly" }
        .configureEach {
            dependencies {
                // fix of package javax.annotation does not exist import javax.annotation.Generated in DataBinding code
                "compileOnly"("javax.annotation:jsr250-api:1.0")
            }
        }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
