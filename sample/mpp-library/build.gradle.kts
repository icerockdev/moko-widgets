/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("android-base-convention")
    id("detekt-convention")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform.android-manifest")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.mobile.multiplatform.ios-framework")
    id("dev.icerock.mobile.multiplatform.cocoapods")
}
kotlin {
    android()
    ios()
}

dependencies {
    commonMainImplementation(libs.coroutines)

    commonMainImplementation(libs.mokoResources)
    commonMainImplementation(libs.mokoMvvm)
    commonMainImplementation(libs.mokoUnits)
    commonMainImplementation(libs.mokoGraphics)
    commonMainImplementation(libs.mokoWidgets)
    commonMainImplementation(libs.mokoWidgetsBottomSheet)
    commonMainImplementation(libs.mokoWidgetsCollection)
    commonMainImplementation(libs.mokoWidgetsDateTimePicker)
    commonMainImplementation(libs.mokoWidgetsImageNetwork)
    commonMainImplementation(libs.mokoWidgetsMedia)
    commonMainImplementation(libs.mokoWidgetsPermissions)
    commonMainImplementation(libs.mokoWidgetsSms)

    "androidMainImplementation"(libs.recyclerView)
    "androidMainImplementation"(libs.appCompat)
    "androidMainImplementation"(libs.material)
}

framework {
    export(libs.mokoWidgets)
    export(libs.mokoWidgetsBottomSheet)
    export(libs.mokoWidgetsCollection)
    export(libs.mokoWidgetsDateTimePicker)
    export(libs.mokoWidgetsImageNetwork)
    export(libs.mokoWidgetsMedia)
    export(libs.mokoWidgetsPermissions)
    export(libs.mokoWidgetsSms)
}

multiplatformResources {
    multiplatformResourcesPackage = "com.icerockdev.library"
}

cocoaPods {
    podsProject = file("../ios-app/Pods/Pods.xcodeproj")

    pod("moko-widgets-bottomsheet", "mokoWidgetsBottomSheet", onlyLink = true)
    pod("moko-widgets-collection", "mokoWidgetsCollection", onlyLink = true)
    pod("moko-widgets-datetime-picker", "mokoWidgetsDateTimePicker", onlyLink = true)
    pod("moko-widgets-image-network", "mokoWidgetsImageNetwork", onlyLink = true)
    pod("mppLibraryIos")
}
