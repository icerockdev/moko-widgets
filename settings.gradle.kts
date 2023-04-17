/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        
        jcenter {
            content {
                includeGroup("org.jetbrains.kotlinx")
                includeGroup("com.redmadrobot")
            }
        }

        maven { url = uri("https://jitpack.io") }
    }
}

includeBuild("widgets-build-logic")

includeBuild("plugin")

include(":widgets")
include(":widgets-sms")
include(":widgets-bottomsheet")
include(":widgets-collection")
include(":widgets-datetime-picker")
include(":widgets-image-network")
include(":widgets-permissions")
include(":widgets-media")

// allow to use moko-widgets as composite build in other projects with `android-app`/`mpp-library` modules
if (gradle.parent == null) {
    include(":sample:android-app")
    include(":sample:mpp-library")
}
