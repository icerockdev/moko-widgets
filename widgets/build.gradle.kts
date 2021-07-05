/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("dev.icerock.mobile.multiplatform")
    id("maven-publish")
}

dependencies {
    commonMainImplementation(libs.kotlinStdLib)

    commonMainImplementation(libs.coroutines)

    commonMainImplementation(libs.mokoResources)
    commonMainImplementation(libs.mokoMvvm)
    commonMainImplementation(libs.mokoFields)
    commonMainImplementation(libs.mokoUnits)
    commonMainImplementation(libs.mokoGraphics)
    commonMainImplementation(libs.mokoParcelize)

    androidMainImplementation(libs.appCompat)
    androidMainImplementation(libs.fragment)
    androidMainImplementation(libs.recyclerView)
    androidMainImplementation(libs.material)
    androidMainImplementation(libs.swipeRefreshLayout)
    androidMainImplementation(libs.constraintLayout)
    androidMainImplementation(libs.inputMask)
    androidMainImplementation(libs.roundedImageView)
}

kotlin {
    targets.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().forEach { target ->
        target.compilations.getByName("main") {
            val objcAddtition by cinterops.creating {
                defFile(project.file("src/iosMain/def/objcAddtition.def"))
                packageName("dev.icerock.moko.widgets.core.objc")
            }
        }
    }
}
