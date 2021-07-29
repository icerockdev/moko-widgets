/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("android-app-convention")
    id("detekt-convention")
    id("kotlin-kapt")
}

android {
    buildFeatures.dataBinding = true

    defaultConfig {
        applicationId = "dev.icerock.moko.samples.widgets"

        versionCode = 1
        versionName = "0.1.0"
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appCompat)
    implementation(libs.recyclerView)
    implementation(libs.material)
    implementation(libs.constraintLayout)

    implementation(projects.widgets)
    implementation(projects.sample.mppLibrary)
}
