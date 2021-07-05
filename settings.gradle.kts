/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("GRADLE_METADATA")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()

        jcenter()
    }
}

includeBuild("widgets-build-logic")
includeBuild("plugin")

include(":widgets")
include(":widgets-flat")
include(":widgets-sms")
include(":widgets-bottomsheet")
include(":widgets-collection")
include(":widgets-datetime-picker")
include(":widgets-image-network")
include(":widgets-permissions")
include(":widgets-media")
include(":sample:android-app")
include(":sample:mpp-library")
