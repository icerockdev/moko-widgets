/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
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
    "androidMainImplementation"(libs.appCompat)
    "androidMainImplementation"(libs.fragment)
    "androidMainImplementation"(libs.recyclerView)
    "androidMainImplementation"(libs.material)
    "androidMainImplementation"(libs.swipeRefreshLayout)
    "androidMainImplementation"(libs.constraintLayout)
    "androidMainImplementation"(libs.inputMask)
    "androidMainImplementation"(libs.roundedImageView)
}

kotlin {
    targets.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>()
        .forEach { target ->
            target.compilations.getByName("main") {
                val objcAddtition by cinterops.creating {
                    defFile(project.file("src/iosMain/def/objcAddtition.def"))
                    packageName("dev.icerock.moko.widgets.core.objc")
                }
            }
        }
}
