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
        classpath("dev.icerock.moko:resources-generator:0.16.1")
        classpath("gradle.plugin.com.github.kukuhyoniatmoko:buildconfigkotlin:1.0.5")
        classpath("dev.icerock.moko.widgets:gradle-plugin")
        
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
    
    configurations.all {
        resolutionStrategy.dependencySubstitution {
            substitute(module("com.nbsp:library"))
                .using(module("com.github.icerockdev:MaterialFilePicker:1.9.1"))
                .because("androidx support in new artifact")
        }
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
