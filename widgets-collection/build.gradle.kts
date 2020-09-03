/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    plugin(Deps.Plugins.androidLibrary)
    plugin(Deps.Plugins.mokoWidgets)
    plugin(Deps.Plugins.kotlinMultiplatform)
    plugin(Deps.Plugins.kotlinCocoapods)
    plugin(Deps.Plugins.mobileMultiplatform)
    plugin(Deps.Plugins.mavenPublish)
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

//kotlin {
//    cocoapods {
//        summary = "CocoaPods test library"
//        homepage = "https://github.com/JetBrains/kotlin"
//
////        noPodspec()
//
//        ios.deploymentTarget = "11.0"
//
//        pod(
//            name = "mokoWidgetsCollection",
//            version = "0.1.0",
//            podspec = rootProject.file("mokoWidgetsCollection.podspec")
//        )
//    }
//}

//cocoaPods {
//    podsProject = file("../sample/ios-app/Pods/Pods.xcodeproj")
//
//    pod(scheme = "moko-widgets-collection", module = "mokoWidgetsCollection")
//}
