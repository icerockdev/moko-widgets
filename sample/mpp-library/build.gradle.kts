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

setupFramework(exports = emptyList())

dependencies {
    commonMainImplementation(libs.kotlinStdLib)
    commonMainImplementation(libs.coroutines)
    commonMainImplementation(libs.mokoResources)
    commonMainImplementation(libs.mokoMvvm)
    commonMainImplementation(libs.mokoUnits)
    commonMainImplementation(libs.mokoGraphics)
    commonMainImplementation(libs.mokoWidgets)
    commonMainImplementation(libs.mokoWidgetsBottomSheet)
    commonMainImplementation(libs.mokoWidgetsCollection)
    commonMainImplementation(libs.mokoWidgetsDateTimePicker)
    commonMainImplementation(libs.mokoWidgetsFlat)
    commonMainImplementation(libs.mokoWidgetsImageNetwork)
    commonMainImplementation(libs.mokoWidgetsMedia)
    commonMainImplementation(libs.mokoWidgetsPermissions)
    commonMainImplementation(libs.mokoWidgetsSms)

    androidMainImplementation(libs.recyclerView)
    androidMainImplementation(libs.appCompat)
    androidMainImplementation(libs.material)
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
