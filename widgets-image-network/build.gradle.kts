/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("org.gradle.maven-publish")
}

dependencies {
    commonMainApi(project(":widgets"))

    commonMainImplementation(Deps.Libs.MultiPlatform.coroutines)

    androidMainImplementation(Deps.Libs.Android.lifecycle)
    androidMainImplementation(Deps.Libs.Android.glide)
}

cocoaPods {
    pod("moko-widgets-image-network", "mokoWidgetsImageNetwork")
}
