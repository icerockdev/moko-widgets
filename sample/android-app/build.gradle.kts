/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(Versions.Android.compileSdk)

    dataBinding {
        isEnabled = true
    }

    dexOptions {
        javaMaxHeapSize = "2g"
    }

    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
        targetSdkVersion(Versions.Android.targetSdk)

        applicationId = "dev.icerock.moko.samples.widgets"

        versionCode = 1
        versionName = Versions.Libs.MultiPlatform.mokoWidgets

        vectorDrawables.useSupportLibrary = true

        multiDexEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation(libs.kotlinStdLib)

    implementation(libs.appCompat)
    implementation(libs.recyclerView)
    implementation(libs.material)
    implementation(libs.constraintLayout)

    implementation(libs.mokoWidgets)
    implementation(project(":sample:mpp-library"))
}
