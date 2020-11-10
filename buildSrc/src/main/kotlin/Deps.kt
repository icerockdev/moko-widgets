/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

object Deps {
    private const val kotlinVersion = "1.4.10"
    private const val autoServiceVersion = "1.0-rc6"

    private const val androidAppCompatVersion = "1.1.0"
    private const val fragmentVersion = "1.2.2"
    private const val swipeRefreshLayoutVersion = "1.0.0"
    private const val materialVersion = "1.1.0"
    private const val constraintLayoutVersion = "1.1.3"
    private const val lifecycleVersion = "2.0.0"
    private const val recyclerViewVersion = "1.0.0"
    private const val inputMaskVersion = "5.0.0"
    private const val glideVersion = "4.10.0"
    private const val roundedImageViewVersion = "2.3.0"
    private const val playServiceAuthVersion = "17.0.0"
    private const val playServiceAuthSmsVersion = "17.1.0"
    private const val multiDexVersion = "2.0.1"

    private const val detektVersion = "1.7.4"

    private const val klockVersion = "1.12.0"
    private const val coroutinesVersion = "1.3.9"
    private const val mokoGraphicsVersion = "0.4.0"
    private const val mokoParcelizeVersion = "0.4.0"
    private const val mokoResourcesVersion = "0.13.0"
    private const val mokoMvvmVersion = "0.8.0"
    private const val mokoFieldsVersion = "0.5.0"
    private const val mokoUnitsVersion = "0.4.0"
    private const val mokoMediaVersion = "0.5.0"
    private const val mokoPermissionsVersion = "0.6.0"
    const val mokoWidgetsVersion = BuildConfig.widgetsVersion

    object Android {
        const val compileSdk = 28
        const val targetSdk = 28
        const val minSdk = 16
    }

    object Plugins {
        val javaGradle = GradlePlugin(id = "java-gradle-plugin")
        val androidApplication = GradlePlugin(id = "com.android.application")
        val androidLibrary = GradlePlugin(id = "com.android.library")
        val kotlinJvm = GradlePlugin(id = "org.jetbrains.kotlin.jvm")
        val kotlinMultiplatform = GradlePlugin(id = "org.jetbrains.kotlin.multiplatform")
        val kotlinCocoapods = GradlePlugin(id = "org.jetbrains.kotlin.native.cocoapods")
        val kotlinKapt = GradlePlugin(id = "kotlin-kapt")
        val kotlinAndroid = GradlePlugin(id = "kotlin-android")
        val kotlinAndroidExtensions = GradlePlugin(id = "kotlin-android-extensions")
        val kotlinSerialization = GradlePlugin(id = "kotlin-serialization")
        val mavenPublish = GradlePlugin(id = "org.gradle.maven-publish")

        val mobileMultiplatform = GradlePlugin(id = "dev.icerock.mobile.multiplatform")
        val iosFramework = GradlePlugin(id = "dev.icerock.mobile.multiplatform.ios-framework")

        val mokoResources = GradlePlugin(
            id = "dev.icerock.mobile.multiplatform-resources",
            module = "dev.icerock.moko:resources-generator:$mokoResourcesVersion"
        )
        val mokoWidgets = GradlePlugin(
            id = "dev.icerock.mobile.multiplatform-widgets-generator",
            module = "dev.icerock.moko.widgets:gradle-plugin:$mokoWidgetsVersion"
        )

        val buildKonfig = GradlePlugin(
            id = "com.github.kukuhyoniatmoko.buildconfigkotlin",
            version = "1.0.5"
        )

        val detekt = GradlePlugin(
            id = "io.gitlab.arturbosch.detekt",
            version = detektVersion
        )
    }

    object Libs {
        object Android {
            const val appCompat =
                "androidx.appcompat:appcompat:$androidAppCompatVersion"
            const val fragment =
                "androidx.fragment:fragment:$fragmentVersion"
            const val swipeRefreshLayout =
                "androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion"
            const val material =
                "com.google.android.material:material:$materialVersion"
            const val recyclerView =
                "androidx.recyclerview:recyclerview:$recyclerViewVersion"
            const val constraintLayout =
                "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
            const val lifecycle =
                "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
            const val inputMask =
                "com.redmadrobot:input-mask-android:$inputMaskVersion"
            const val glide =
                "com.github.bumptech.glide:glide:$glideVersion"
            const val roundedImageView =
                "com.makeramen:roundedimageview:$roundedImageViewVersion"
            const val playServiceAuth =
                "com.google.android.gms:play-services-auth:$playServiceAuthVersion"
            const val playServiceAuthSms =
                "com.google.android.gms:play-services-auth-api-phone:$playServiceAuthSmsVersion"
            const val multiDex =
                "androidx.multidex:multidex:$multiDexVersion"
        }

        object MultiPlatform {
            val mokoWidgets = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-iosarm64:$mokoWidgetsVersion"
            )
            val mokoWidgetsBottomSheet = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-bottomsheet:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-bottomsheet-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-bottomsheet-iosarm64:$mokoWidgetsVersion"
            )
            val mokoWidgetsCollection = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-collection:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-collection-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-collection-iosarm64:$mokoWidgetsVersion"
            )
            val mokoWidgetsDateTimePicker = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-datetime-picker:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-datetime-picker-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-datetime-picker-iosarm64:$mokoWidgetsVersion"
            )
            val mokoWidgetsImageNetwork = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-image-network:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-image-network-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-image-network-iosarm64:$mokoWidgetsVersion"
            )
            val mokoWidgetsPermissions = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-permissions:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-permissions-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-permissions-iosarm64:$mokoWidgetsVersion"
            )
            val mokoWidgetsMedia = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-media:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-media-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-media-iosarm64:$mokoWidgetsVersion"
            )
            val mokoWidgetsSms = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets-sms:$mokoWidgetsVersion",
                iosX64 = "dev.icerock.moko:widgets-sms-iosx64:$mokoWidgetsVersion",
                iosArm64 = "dev.icerock.moko:widgets-sms-iosarm64:$mokoWidgetsVersion"
            )
            val mokoResources = MultiPlatformLibrary(
                common = "dev.icerock.moko:resources:$mokoResourcesVersion",
                iosX64 = "dev.icerock.moko:resources-iosx64:$mokoResourcesVersion",
                iosArm64 = "dev.icerock.moko:resources-iosarm64:$mokoResourcesVersion"
            )
            val mokoMvvm = MultiPlatformLibrary(
                common = "dev.icerock.moko:mvvm:$mokoMvvmVersion",
                iosX64 = "dev.icerock.moko:mvvm-iosx64:$mokoMvvmVersion",
                iosArm64 = "dev.icerock.moko:mvvm-iosarm64:$mokoMvvmVersion"
            )
            val mokoFields = MultiPlatformLibrary(
                common = "dev.icerock.moko:fields:$mokoFieldsVersion",
                iosX64 = "dev.icerock.moko:fields-iosx64:$mokoFieldsVersion",
                iosArm64 = "dev.icerock.moko:fields-iosarm64:$mokoFieldsVersion"
            )
            val mokoUnits = MultiPlatformLibrary(
                common = "dev.icerock.moko:units:$mokoUnitsVersion",
                iosX64 = "dev.icerock.moko:units-iosx64:$mokoUnitsVersion",
                iosArm64 = "dev.icerock.moko:units-iosarm64:$mokoUnitsVersion"
            )
            val mokoMedia = MultiPlatformLibrary(
                common = "dev.icerock.moko:media:$mokoMediaVersion",
                iosX64 = "dev.icerock.moko:media-iosx64:$mokoMediaVersion",
                iosArm64 = "dev.icerock.moko:media-iosarm64:$mokoMediaVersion"
            )
            val mokoPermissions = MultiPlatformLibrary(
                common = "dev.icerock.moko:permissions:$mokoPermissionsVersion",
                iosX64 = "dev.icerock.moko:permissions-iosx64:$mokoPermissionsVersion",
                iosArm64 = "dev.icerock.moko:permissions-iosarm64:$mokoPermissionsVersion"
            )
            val mokoGraphics = MultiPlatformLibrary(
                common = "dev.icerock.moko:graphics:$mokoGraphicsVersion",
                iosX64 = "dev.icerock.moko:graphics-iosx64:$mokoGraphicsVersion",
                iosArm64 = "dev.icerock.moko:graphics-iosarm64:$mokoGraphicsVersion"
            )
            val mokoParcelize = MultiPlatformLibrary(
                common = "dev.icerock.moko:parcelize:$mokoParcelizeVersion",
                iosX64 = "dev.icerock.moko:parcelize-iosx64:$mokoParcelizeVersion",
                iosArm64 = "dev.icerock.moko:parcelize-iosarm64:$mokoParcelizeVersion"
            )
            const val coroutines =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val klock =
                "com.soywiz.korlibs.klock:klock:$klockVersion"
        }

        object Jvm {
            const val detektFormatting =
                "io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion"
            const val kotlinGradlePlugin =
                "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
            const val kotlinGradlePluginApi =
                "org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion"
            const val autoService =
                "com.google.auto.service:auto-service:$autoServiceVersion"
            const val kotlinCompilerEmbeddable =
                "org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion"
        }
    }
}
