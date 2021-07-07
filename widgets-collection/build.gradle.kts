/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("maven-publish")
}

dependencies {
    commonMainApi(project(":widgets"))

    commonMainImplementation(libs.kotlinStdLib)
    commonMainImplementation(libs.coroutines)

    commonMainImplementation(libs.mokoMvvm)
    commonMainImplementation(libs.mokoResources)
    commonMainImplementation(libs.mokoFields)
    commonMainImplementation(libs.mokoUnits)

    androidMainImplementation(libs.lifecycle)
    androidMainImplementation(libs.recyclerView)
    androidMainImplementation(libs.swipeRefreshLayout)
}

cocoaPods {
    podsProject = file("../sample/ios-app/Pods/Pods.xcodeproj")

    pod(scheme = "moko-widgets-collection", module = "mokoWidgetsCollection")
}
