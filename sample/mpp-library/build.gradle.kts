/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("dev.icerock.mobile.multiplatform-units")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Versions.Android.compileSdk)

    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
        targetSdkVersion(Versions.Android.targetSdk)
    }

    dataBinding {
        isEnabled = true
    }
}

setupFramework(
    exports = listOf(
        Deps.Libs.MultiPlatform.mokoUnits
    )
)

dependencies {
    mppLibrary(Deps.Libs.MultiPlatform.kotlinStdLib)
    mppLibrary(Deps.Libs.MultiPlatform.mokoUnits)

    androidLibrary(Deps.Libs.Android.recyclerView)
}

multiplatformUnits {
    classesPackage = "com.icerockdev.library"
    dataBindingPackage = "com.icerockdev.library"
    layoutsSourceSet = "androidMain"
}
