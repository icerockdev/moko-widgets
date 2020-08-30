/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

subprojects {
    configurations.configureEach {
        resolutionStrategy.dependencySubstitution {
            listOf(
                "widgets",
                "widgets-bottomsheet",
                "widgets-collection",
                "widgets-datetime-picker",
                "widgets-image-network",
                "widgets-media",
                "widgets-permissions",
                "widgets-sms"
            ).forEach { library ->
                substitute(module("dev.icerock.moko:$library"))
                    .with(project(":$library"))
                substitute(module("dev.icerock.moko:$library-iosarm64"))
                    .with(project(":$library"))
                substitute(module("dev.icerock.moko:$library-iosx64"))
                    .with(project(":$library"))
            }
        }
    }
}
