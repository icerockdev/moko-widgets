/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
    id("org.gradle.maven-publish")
}

dependencies {
    commonMainApi(project(":widgets"))

    commonMainImplementation(Deps.Libs.MultiPlatform.coroutines)

    commonMainApi(Deps.Libs.MultiPlatform.mokoMvvm.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoResources.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoFields.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoUnits.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoGraphics.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoParcelize.common)

    androidMainImplementation(Deps.Libs.Android.lifecycle)
    androidMainImplementation(Deps.Libs.Android.recyclerView)
    androidMainImplementation(Deps.Libs.Android.swipeRefreshLayout)
}

cocoaPods {
    pod(scheme = "moko-widgets-collection", module = "mokoWidgetsCollection")
}
