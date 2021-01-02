/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(Deps.Android.compileSdk)

    buildFeatures.dataBinding = true

    dexOptions {
        javaMaxHeapSize = "2g"
    }

    defaultConfig {
        minSdkVersion(Deps.Android.minSdk)
        targetSdkVersion(Deps.Android.targetSdk)

        applicationId = "dev.icerock.moko.samples.widgets"

        versionCode = 1
        versionName = Deps.mokoWidgetsVersion

        vectorDrawables.useSupportLibrary = true
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(Deps.Libs.Android.appCompat)
    implementation(Deps.Libs.Android.recyclerView)
    implementation(Deps.Libs.Android.material)
    implementation(Deps.Libs.Android.constraintLayout)

    implementation(Deps.Libs.MultiPlatform.mokoWidgets.common)
    implementation(project(":sample:mpp-library"))
}
