/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

pluginManagement {
    repositories {
        mavenLocal()

        jcenter()
        google()

        maven { url = uri("https://dl.bintray.com/kotlin/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
    }
    resolutionStrategy.eachPlugin {
        when(requested.id.id) {
            "dev.icerock.mobile.multiplatform-widgets-generator" -> useModule("dev.icerock.moko.widgets:gradle-plugin:0.1.0")
        }
    }
}

enableFeaturePreview("GRADLE_METADATA")

include(":widgets")
include(":kotlin-plugin")
include(":kotlin-native-plugin")
include(":gradle-plugin")
include(":sample:android-app")
include(":sample:mpp-library")
