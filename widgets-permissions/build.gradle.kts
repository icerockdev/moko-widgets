/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
    id("publication-convention")
}

dependencies {
    commonMainApi(projects.widgets)

    commonMainImplementation(libs.coroutines)

    commonMainApi(libs.mokoPermissions)

    "androidMainImplementation"(libs.lifecycle)
}
