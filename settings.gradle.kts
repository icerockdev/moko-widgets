/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

enableFeaturePreview("GRADLE_METADATA")

val properties = startParameter.projectProperties

val pluginPublish: Boolean = properties.containsKey("pluginPublish")
val corePublish: Boolean = properties.containsKey("corePublish")
val additionsPublish: Boolean = properties.containsKey("additionsPublish")

include(":kotlin-common-plugin")
include(":kotlin-plugin")
include(":kotlin-native-plugin")
include(":gradle-plugin")

if (!pluginPublish) {
    include(":widgets")

    if(!corePublish) {
        include(":widgets-flat")
        include(":widgets-sms")
        include(":widgets-bottomsheet")
        include(":widgets-collection")
        include(":widgets-datetime-picker")
        include(":widgets-image-network")
        include(":widgets-permissions")
        include(":widgets-media")

        if (!additionsPublish) {
            include(":sample:android-app")
            include(":sample:mpp-library")
        }
    }
}
