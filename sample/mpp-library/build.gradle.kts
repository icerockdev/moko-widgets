/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    plugin(Deps.Plugins.androidLibrary)
    plugin(Deps.Plugins.kotlinMultiplatform)
    plugin(Deps.Plugins.mobileMultiplatform)
    plugin(Deps.Plugins.kotlinAndroidExtensions)
    plugin(Deps.Plugins.mokoResources)
    plugin(Deps.Plugins.kotlinKapt)
    plugin(Deps.Plugins.iosFramework)
}

android {
    buildFeatures.dataBinding = true

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
    Deps.Libs.MultiPlatform.mokoWidgetsImageNetwork,
    Deps.Libs.MultiPlatform.mokoWidgetsMedia,
    Deps.Libs.MultiPlatform.mokoWidgetsPermissions,
    Deps.Libs.MultiPlatform.mokoWidgetsSms
)

dependencies {
    commonMainImplementation(Deps.Libs.MultiPlatform.coroutines)

    deps.forEach { commonMainImplementation(it.common) }

    androidMainImplementation(Deps.Libs.Android.recyclerView)
    androidMainImplementation(Deps.Libs.Android.appCompat)
    androidMainImplementation(Deps.Libs.Android.material)
}

framework {

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
