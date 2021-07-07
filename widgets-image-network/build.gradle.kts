/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("maven-publish")
}

dependencies {
    commonMainApi(project(":widgets"))

    commonMainImplementation(libs.kotlinStdLib)
    commonMainImplementation(libs.coroutines)

    androidMainImplementation(libs.lifecycle)
    androidMainImplementation(libs.glide)
}

cocoaPods {
    podsProject = file("../sample/ios-app/Pods/Pods.xcodeproj")

    pod("moko-widgets-image-network", "mokoWidgetsImageNetwork")
}
