/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("kotlin-android-extensions")
    id("maven-publish")
}

dependencies {
    commonMainApi(project(":widgets"))

    mppLibrary(Deps.Libs.MultiPlatform.kotlinStdLib)
    mppLibrary(Deps.Libs.MultiPlatform.coroutines)

    mppLibrary(Deps.Libs.MultiPlatform.mokoMvvm)
    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
    mppLibrary(Deps.Libs.MultiPlatform.klock)

    androidLibrary(Deps.Libs.Android.appCompat)
    androidLibrary(Deps.Libs.Android.lifecycle)
}

cocoaPods {
    podsProject = file("../sample/ios-app/Pods/Pods.xcodeproj")

    pod("moko-widgets-datetime-picker", "mokoWidgetsDateTimePicker")
}
