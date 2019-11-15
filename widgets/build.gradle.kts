import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlin-kapt")
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

    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
    mppLibrary(Deps.Libs.MultiPlatform.mokoMvvm)
    mppLibrary(Deps.Libs.MultiPlatform.mokoFields)
    mppLibrary(Deps.Libs.MultiPlatform.mokoUnits)
    mppLibrary(Deps.Libs.MultiPlatform.mokoMedia)
    mppLibrary(Deps.Libs.MultiPlatform.mokoGraphics)
    mppLibrary(Deps.Libs.MultiPlatform.mokoParcelize)
    mppLibrary(
        MultiPlatformLibrary(
            common = "dev.icerock.moko:core:0.1.0"
        )
    )

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

kotlin {
    targets.filterIsInstance<KotlinNativeTarget>().forEach { target ->
        target.compilations.getByName("main") {
            val pluralsFormat by cinterops.creating {
                defFile(project.file("src/iosMain/def/stringFormat.def"))
                packageName("dev.icerock.plural")
            }
        }
    }
}
