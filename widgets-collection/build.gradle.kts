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

    mppLibrary(Deps.Libs.MultiPlatform.kotlinStdLib)
    mppLibrary(Deps.Libs.MultiPlatform.coroutines)

    mppLibrary(Deps.Libs.MultiPlatform.mokoMvvm)
    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
    mppLibrary(Deps.Libs.MultiPlatform.mokoFields)
    mppLibrary(Deps.Libs.MultiPlatform.mokoUnits)

    androidLibrary(Deps.Libs.Android.lifecycle)
    androidLibrary(Deps.Libs.Android.recyclerView)
    androidLibrary(Deps.Libs.Android.swipeRefreshLayout)
}

cocoaPods {
    podsProject = file("../sample/ios-app/Pods/Pods.xcodeproj")

    pod(scheme = "moko-widgets-collection", module = "mokoWidgetsCollection")
}
