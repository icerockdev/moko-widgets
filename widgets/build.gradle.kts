/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("dev.icerock.mobile.multiplatform")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("dev.icerock.mobile.multiplatform-widgets-generator")
    id("org.gradle.maven-publish")
}

dependencies {
    commonMainImplementation(Deps.Libs.MultiPlatform.coroutines)

    commonMainApi(Deps.Libs.MultiPlatform.mokoResources.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoMvvm.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoFields.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoUnits.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoGraphics.common)
    commonMainApi(Deps.Libs.MultiPlatform.mokoParcelize.common)

    androidMainImplementation(Deps.Libs.Android.appCompat)
    androidMainImplementation(Deps.Libs.Android.fragment)
    androidMainImplementation(Deps.Libs.Android.recyclerView)
    androidMainImplementation(Deps.Libs.Android.material)
    androidMainImplementation(Deps.Libs.Android.swipeRefreshLayout)
    androidMainImplementation(Deps.Libs.Android.constraintLayout)
    androidMainImplementation(Deps.Libs.Android.inputMask)
    androidMainImplementation(Deps.Libs.Android.roundedImageView)
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
