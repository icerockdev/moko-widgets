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
        val module = Deps.plugins[requested.id.id] ?: return@eachPlugin

        useModule(module)
    }
}

enableFeaturePreview("GRADLE_METADATA")

val properties = startParameter.projectProperties

// ./gradlew -PpluginPublish publishPluginPublicationToMavenLocal
val pluginPublish: Boolean = properties.containsKey("pluginPublish")

// ./gradlew -PlibraryPublish :widgets:publishToMavenLocal
// ./gradlew -PlibraryPublish :widgets-flat:publishToMavenLocal :widgets-bottomsheet:publishToMavenLocal
val libraryPublish: Boolean = properties.containsKey("libraryPublish")

include(":kotlin-common-plugin")
include(":kotlin-plugin")
include(":kotlin-native-plugin")
include(":gradle-plugin")

if (!pluginPublish) {
    include(":widgets")
    include(":widgets-flat")
    include(":widgets-bottomsheet")

    if (!libraryPublish) {
        include(":sample:android-app")
        include(":sample:mpp-library")
    }
}
