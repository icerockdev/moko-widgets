/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("kotlin-android-extensions")
    id("dev.icerock.mobile.multiplatform-resources")
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

val deps = listOf(
    Deps.Libs.MultiPlatform.mokoResources,
    Deps.Libs.MultiPlatform.mokoMvvm,
    Deps.Libs.MultiPlatform.mokoUnits,
    Deps.Libs.MultiPlatform.mokoGraphics,
    Deps.Libs.MultiPlatform.mokoWidgets,
    Deps.Libs.MultiPlatform.mokoWidgetsFlat,
    Deps.Libs.MultiPlatform.mokoWidgetsBottomSheet
)

setupFramework(
    exports = deps
)

dependencies {
    mppLibrary(Deps.Libs.MultiPlatform.kotlinStdLib)
    mppLibrary(Deps.Libs.MultiPlatform.coroutines)

    deps.forEach { mppLibrary(it) }

    androidLibrary(Deps.Libs.Android.recyclerView)
    androidLibrary(Deps.Libs.Android.appCompat)
    androidLibrary(Deps.Libs.Android.material)
}

multiplatformResources {
    multiplatformResourcesPackage = "com.icerockdev.library"
}

cocoaPods {
    podsProject = file("../ios-app/Pods/Pods.xcodeproj")

    pod("moko-widgets-flat", "mokoWidgetsFlat", onlyLink = true)
    pod("moko-widgets-bottom-sheet", "mokoWidgetsBottomSheet", onlyLink = true)
    pod("mppLibraryIos")
}
