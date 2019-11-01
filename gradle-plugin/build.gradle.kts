/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("kotlin-kapt")
}

dependencies {
    implementation(Deps.Libs.MultiPlatform.kotlinStdLib.android!!)

    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.3.50")

    compileOnly("com.google.auto.service:auto-service:1.0-rc6")
    kapt("com.google.auto.service:auto-service:1.0-rc6")
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
            version = Versions.Libs.MultiPlatform.mokoWidgets

            from(components["java"])
        }
    }
}
