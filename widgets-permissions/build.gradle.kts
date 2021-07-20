/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
    id("publication-convention")
    
    id("dev.icerock.mobile.multiplatform.ios-framework")
}

dependencies {
    commonMainApi(projects.widgets)

    commonMainImplementation(libs.coroutines)

    commonMainApi(libs.mokoPermissions)
    commonMainApi(libs.mokoMvvmCore)


    "androidMainImplementation"(libs.lifecycle)
}

framework {
    export(libs.mokoPermissions)
    export(libs.mokoMvvmCore)

}