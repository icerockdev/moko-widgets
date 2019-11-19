/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("kotlin-kapt")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

dependencies {
    implementation(Deps.Libs.MultiPlatform.kotlinStdLib.android!!)

    compileOnly(Deps.Libs.Jvm.kotlinGradlePlugin)
    implementation(Deps.Libs.Jvm.kotlinGradlePluginApi)

    compileOnly(Deps.Libs.Jvm.autoService)
    kapt(Deps.Libs.Jvm.autoService)
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "compilerPluginVersion", value = Versions.Plugins.mokoWidgets)
    }
}

publishing {
    repositories.maven("https://api.bintray.com/maven/icerockdev/plugins/moko-widgets-generator/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getProperty("BINTRAY_USER")
            password = System.getProperty("BINTRAY_KEY")
        }
    }

    publications {
        register("plugin", MavenPublication::class) {
            groupId = "dev.icerock.moko.widgets"
            artifactId = "gradle-plugin"
            version = Versions.Plugins.mokoWidgets

            from(components["java"])
        }
    }
}
