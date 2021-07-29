/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("publication-convention")
    id("dev.icerock.mobile.multiplatform.cocoapods")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
}

group = "dev.icerock.moko"
version = libs.versions.mokoWidgetsVersion.get()

dependencies {
    commonMainApi(projects.widgets)

    commonMainImplementation(libs.coroutines)

    commonMainApi(libs.mokoMvvmCore)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoFields)
    commonMainApi(libs.mokoUnits)
    commonMainApi(libs.mokoGraphics)
    commonMainApi(libs.mokoParcelize)

    androidMainImplementation(libs.lifecycleViewModel)
    androidMainImplementation(libs.recyclerView)
    androidMainImplementation(libs.swipeRefreshLayout)
}

cocoaPods {
    pod(scheme = "moko-widgets-collection", module = "mokoWidgetsCollection")
}
