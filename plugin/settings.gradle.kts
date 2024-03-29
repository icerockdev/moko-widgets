/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        mavenCentral()
        google()

        gradlePluginPortal()
        jcenter()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()

        jcenter {
            content {
                includeGroup("org.jetbrains.kotlinx")
            }
        }
        
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

includeBuild("../widgets-build-logic")

include(":kotlin-plugin")
include(":gradle-plugin")
