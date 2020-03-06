/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("maven-publish")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
}

group = "dev.icerock.moko"
version = Versions.Libs.MultiPlatform.mokoWidgets

android {
    compileSdkVersion(Versions.Android.compileSdk)

    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
        targetSdkVersion(Versions.Android.targetSdk)
    }
}

dependencies {
    mppLibrary(Deps.Libs.MultiPlatform.kotlinStdLib)
    mppLibrary(Deps.Libs.MultiPlatform.coroutines)

    mppLibrary(Deps.Libs.MultiPlatform.mokoMvvm)
    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
    mppLibrary(Deps.Libs.MultiPlatform.mokoFields)
    mppLibrary(Deps.Libs.MultiPlatform.mokoWidgets)
    mppLibrary(Deps.Libs.MultiPlatform.mokoUnits)

    androidLibrary(Deps.Libs.Android.lifecycle)
    androidLibrary(Deps.Libs.Android.recyclerView)
    androidLibrary(Deps.Libs.Android.swipeRefreshLayout)
}

publishing {
    repositories.maven("https://api.bintray.com/maven/icerockdev/moko/moko-widgets/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getProperty("BINTRAY_USER")
            password = System.getProperty("BINTRAY_KEY")
        }
    }
}

cocoaPods {
    podsProject = file("../sample/ios-app/Pods/Pods.xcodeproj")

    pod(scheme = "moko-widgets-collection", module = "mokoWidgetsCollection")
}
