/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlin-kapt")
    id("dev.icerock.mobile.multiplatform")
    id("maven-publish")
}

group = "dev.icerock.moko"
version = Versions.Libs.MultiPlatform.mokoWidgets

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

dependencies {
    mppLibrary(Deps.Libs.MultiPlatform.kotlinStdLib)

    mppLibrary(Deps.Libs.MultiPlatform.coroutines)

    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
    mppLibrary(Deps.Libs.MultiPlatform.mokoMvvm)
    mppLibrary(Deps.Libs.MultiPlatform.mokoFields)
    mppLibrary(Deps.Libs.MultiPlatform.mokoUnits)
    mppLibrary(Deps.Libs.MultiPlatform.mokoMedia)

    androidLibrary(Deps.Libs.Android.appCompat)
    androidLibrary(Deps.Libs.Android.recyclerView)
    androidLibrary(Deps.Libs.Android.material)
    androidLibrary(Deps.Libs.Android.constraintLayout)
    androidLibrary(Deps.Libs.Android.inputMask)
    androidLibrary(Deps.Libs.Android.glide)
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
