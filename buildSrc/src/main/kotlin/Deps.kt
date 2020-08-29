/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

object Deps {
    object Plugins {
        const val mokoWidgets =
            "dev.icerock.moko.widgets:gradle-plugin:${Versions.Plugins.mokoWidgets}"
        const val mokoResources =
            "dev.icerock.moko:resources-generator:${Versions.Plugins.mokoResources}"
    }

    object Libs {
        object Android {
            val kotlinStdLib = AndroidLibrary(
                name = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
            )
            val appCompat = AndroidLibrary(
                name = "androidx.appcompat:appcompat:${Versions.Libs.Android.appCompat}"
            )
            val fragment = AndroidLibrary(
                name = "androidx.fragment:fragment:${Versions.Libs.Android.fragment}"
            )
            val swipeRefreshLayout = AndroidLibrary(
                name = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.Libs.Android.swipeRefreshLayout}"
            )
            val material = AndroidLibrary(
                name = "com.google.android.material:material:${Versions.Libs.Android.material}"
            )
            val recyclerView = AndroidLibrary(
                name = "androidx.recyclerview:recyclerview:${Versions.Libs.Android.recyclerView}"
            )
            val constraintLayout = AndroidLibrary(
                name = "androidx.constraintlayout:constraintlayout:${Versions.Libs.Android.constraintLayout}"
            )
            val lifecycle = AndroidLibrary(
                name = "androidx.lifecycle:lifecycle-extensions:${Versions.Libs.Android.lifecycle}"
            )
            val inputMask = AndroidLibrary(
                name = "com.redmadrobot:input-mask-android:${Versions.Libs.Android.inputMask}"
            )
            val glide = AndroidLibrary(
                name = "com.github.bumptech.glide:glide:${Versions.Libs.Android.glide}"
            )
            val roundedImageView = AndroidLibrary(
                name = "com.makeramen:roundedimageview:${Versions.Libs.Android.roundedImageView}"
            )
            val playServiceAuth = AndroidLibrary(
                name = "com.google.android.gms:play-services-auth:${Versions.Libs.Android.playServiceAuth}"
            )
            val playServiceAuthSms = AndroidLibrary(
                name = "com.google.android.gms:play-services-auth-api-phone:${Versions.Libs.Android.playServiceAuthSms}"
            )
        }

        object MultiPlatform {
            val kotlinStdLib = MultiPlatformLibrary(
                android = Android.kotlinStdLib.name,
                common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
            )
            val mokoWidgets = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsFlat = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-flat:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-flat-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-flat-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsBottomSheet = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-bottomsheet:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-bottomsheet-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-bottomsheet-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsCollection = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-collection:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-collection-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-collection-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsDateTimePicker = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-datetime-picker:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-datetime-picker-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-datetime-picker-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsImageNetwork = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-image-network:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-image-network-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-image-network-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsPermissions = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-permissions:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-permissions-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-permissions-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsMedia = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-media:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-media-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-media-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoWidgetsSms = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-sms:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-sms-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-sms-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
            val mokoResources = MultiPlatformLibrary(
                common = "dev.icerock.moko:resources:${Versions.Libs.MultiPlatform.mokoResources}",
                iosX64 = "dev.icerock.moko:resources-iosx64:${Versions.Libs.MultiPlatform.mokoResources}",
                iosArm64 = "dev.icerock.moko:resources-iosarm64:${Versions.Libs.MultiPlatform.mokoResources}"
            )
            val mokoMvvm = MultiPlatformLibrary(
                common = "dev.icerock.moko:mvvm:${Versions.Libs.MultiPlatform.mokoMvvm}",
                iosX64 = "dev.icerock.moko:mvvm-iosx64:${Versions.Libs.MultiPlatform.mokoMvvm}",
                iosArm64 = "dev.icerock.moko:mvvm-iosarm64:${Versions.Libs.MultiPlatform.mokoMvvm}"
            )
            val mokoFields = MultiPlatformLibrary(
                common = "dev.icerock.moko:fields:${Versions.Libs.MultiPlatform.mokoFields}",
                iosX64 = "dev.icerock.moko:fields-iosx64:${Versions.Libs.MultiPlatform.mokoFields}",
                iosArm64 = "dev.icerock.moko:fields-iosarm64:${Versions.Libs.MultiPlatform.mokoFields}"
            )
            val mokoUnits = MultiPlatformLibrary(
                common = "dev.icerock.moko:units:${Versions.Libs.MultiPlatform.mokoUnits}",
                iosX64 = "dev.icerock.moko:units-iosx64:${Versions.Libs.MultiPlatform.mokoUnits}",
                iosArm64 = "dev.icerock.moko:units-iosarm64:${Versions.Libs.MultiPlatform.mokoUnits}"
            )
            val mokoMedia = MultiPlatformLibrary(
                common = "dev.icerock.moko:media:${Versions.Libs.MultiPlatform.mokoMedia}",
                iosX64 = "dev.icerock.moko:media-iosx64:${Versions.Libs.MultiPlatform.mokoMedia}",
                iosArm64 = "dev.icerock.moko:media-iosarm64:${Versions.Libs.MultiPlatform.mokoMedia}"
            )
            val mokoPermissions = MultiPlatformLibrary(
                common = "dev.icerock.moko:permissions:${Versions.Libs.MultiPlatform.mokoPermissions}",
                iosX64 = "dev.icerock.moko:permissions-iosx64:${Versions.Libs.MultiPlatform.mokoPermissions}",
                iosArm64 = "dev.icerock.moko:permissions-iosarm64:${Versions.Libs.MultiPlatform.mokoPermissions}"
            )
            val mokoGraphics = MultiPlatformLibrary(
                common = "dev.icerock.moko:graphics:${Versions.Libs.MultiPlatform.mokoGraphics}",
                iosX64 = "dev.icerock.moko:graphics-iosx64:${Versions.Libs.MultiPlatform.mokoGraphics}",
                iosArm64 = "dev.icerock.moko:graphics-iosarm64:${Versions.Libs.MultiPlatform.mokoGraphics}"
            )
            val mokoParcelize = MultiPlatformLibrary(
                common = "dev.icerock.moko:parcelize:${Versions.Libs.MultiPlatform.mokoParcelize}",
                iosX64 = "dev.icerock.moko:parcelize-iosx64:${Versions.Libs.MultiPlatform.mokoParcelize}",
                iosArm64 = "dev.icerock.moko:parcelize-iosarm64:${Versions.Libs.MultiPlatform.mokoParcelize}"
            )
            val coroutines = MultiPlatformLibrary(
                android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Libs.MultiPlatform.coroutines}",
                common = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.Libs.MultiPlatform.coroutines}",
                ios = "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.Libs.MultiPlatform.coroutines}"
            )
            val klock = MultiPlatformLibrary(
                android = "com.soywiz.korlibs.klock:klock-android:${Versions.Libs.MultiPlatform.klock}",
                common = "com.soywiz.korlibs.klock:klock:${Versions.Libs.MultiPlatform.klock}",
                ios = "com.soywiz.korlibs.klock:klock:${Versions.Libs.MultiPlatform.klock}"
            )
        }

        object Jvm {
            val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
            val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
            val kotlinGradlePluginApi = "org.jetbrains.kotlin:kotlin-gradle-plugin-api:${Versions.kotlin}"
            val autoService = "com.google.auto.service:auto-service:1.0-rc6"
        }
    }

    val plugins: Map<String, String> = mapOf(
        "dev.icerock.mobile.multiplatform-widgets-generator" to Plugins.mokoWidgets,
        "dev.icerock.mobile.multiplatform-resources" to Plugins.mokoResources
    )
}
