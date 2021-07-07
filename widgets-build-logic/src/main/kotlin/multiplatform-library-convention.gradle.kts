/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("android-base-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
}

kotlin {
    ios()
    android {
        publishLibraryVariants("release", "debug")
    }
    sourceSets {
        val mobileDeviceTest by creating

        val commonTest by getting
        val iosTest by getting
        val androidAndroidTest by getting


        mobileDeviceTest.dependsOn(commonTest)
        iosTest.dependsOn(mobileDeviceTest)
        androidAndroidTest.dependsOn(mobileDeviceTest)

    }
}

