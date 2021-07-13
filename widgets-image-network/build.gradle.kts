/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
    id("publication-convention")
    id("dev.icerock.mobile.multiplatform.cocoapods")
}

dependencies {
    commonMainApi(projects.widgets)

    commonMainImplementation(libs.coroutines)

    androidMainImplementation(libs.lifecycle)
    androidMainImplementation(libs.glide)
}

cocoaPods {
    pod("moko-widgets-image-network", "mokoWidgetsImageNetwork")
}
