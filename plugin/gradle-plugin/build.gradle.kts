/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    plugin(Deps.Plugins.kotlinJvm)
    plugin(Deps.Plugins.mavenPublish)
    plugin(Deps.Plugins.kotlinKapt)
    plugin(Deps.Plugins.buildKonfig)
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    compileOnly(Deps.Libs.Jvm.kotlinGradlePlugin)
    implementation(Deps.Libs.Jvm.kotlinGradlePluginApi)

    compileOnly(Deps.Libs.Jvm.autoService)
    kapt(Deps.Libs.Jvm.autoService)
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "compilerPluginVersion", value = project.version.toString())
    }
}
