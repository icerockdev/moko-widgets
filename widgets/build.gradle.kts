/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("publication-convention")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
    id("detekt-convention")
}

dependencies {
    commonMainImplementation(libs.coroutines)
    commonMainApi(libs.mokoResources)
    commonMainApi(libs.mokoMvvmCore)
    commonMainApi(libs.mokoMvvmLivedata)
    commonMainApi(libs.mokoMvvmState)
    commonMainApi(libs.mokoFields)
    commonMainApi(libs.mokoUnits)
    commonMainApi(libs.mokoGraphics)
    commonMainApi(libs.mokoParcelize)
    androidMainApi(libs.appCompat)
    androidMainApi(libs.fragment)
    androidMainImplementation(libs.recyclerView)
    androidMainImplementation(libs.material)
    androidMainImplementation(libs.swipeRefreshLayout)
    androidMainImplementation(libs.constraintLayout)
    androidMainImplementation(libs.inputMask)
    androidMainImplementation(libs.roundedImageView)
}

kotlin {
    targets
        .matching { it is org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget }
        .configureEach {
            this as org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

            compilations.getByName("main") {
                val objcAddition by cinterops.creating {
                    defFile(project.file("src/iosMain/def/objcAddition.def"))
                }
            }
        }
}
