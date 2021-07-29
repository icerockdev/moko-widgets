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
        classpath("com.gradle.publish:plugin-publish-plugin:0.15.0")
        
        classpath(":widgets-build-logic")
    }
}

allprojects {
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
