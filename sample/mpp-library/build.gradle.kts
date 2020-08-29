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

    lintOptions {
        disable("ImpliedQuantity")
    }
}

val deps = listOf(
    Deps.Libs.MultiPlatform.mokoResources,
    Deps.Libs.MultiPlatform.mokoMvvm,
    Deps.Libs.MultiPlatform.mokoUnits,
    Deps.Libs.MultiPlatform.mokoGraphics,
    Deps.Libs.MultiPlatform.mokoWidgets,
    Deps.Libs.MultiPlatform.mokoWidgetsBottomSheet,
    Deps.Libs.MultiPlatform.mokoWidgetsCollection,
    Deps.Libs.MultiPlatform.mokoWidgetsDateTimePicker,
    Deps.Libs.MultiPlatform.mokoWidgetsFlat,
    Deps.Libs.MultiPlatform.mokoWidgetsImageNetwork,
    Deps.Libs.MultiPlatform.mokoWidgetsMedia,
    Deps.Libs.MultiPlatform.mokoWidgetsPermissions,
    Deps.Libs.MultiPlatform.mokoWidgetsSms
)

setupFramework(exports = emptyList())

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

    pod("moko-widgets-bottomsheet", "mokoWidgetsBottomSheet", onlyLink = true)
    pod("moko-widgets-collection", "mokoWidgetsCollection", onlyLink = true)
    pod("moko-widgets-datetime-picker", "mokoWidgetsDateTimePicker", onlyLink = true)
    pod("moko-widgets-flat", "mokoWidgetsFlat", onlyLink = true)
    pod("moko-widgets-image-network", "mokoWidgetsImageNetwork", onlyLink = true)
    pod("mppLibraryIos")
}
