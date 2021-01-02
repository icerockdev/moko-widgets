/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("kotlin-parcelize")
    id("org.gradle.maven-publish")
}

dependencies {
    commonMainApi(project(":widgets"))

    commonMainImplementation(Deps.Libs.MultiPlatform.coroutines)

    commonMainApi(Deps.Libs.MultiPlatform.mokoMvvm.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoResources.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoGraphics.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoParcelize.common)

    commonMainImplementation(Deps.Libs.MultiPlatform.klock)

    androidMainImplementation(Deps.Libs.Android.appCompat)
    androidMainImplementation(Deps.Libs.Android.lifecycle)
}

cocoaPods {
    pod("moko-widgets-datetime-picker", "mokoWidgetsDateTimePicker")
}
