/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

buildscript {
    repositories {
        jcenter()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath("dev.icerock.moko.widgets:gradle-plugin:0.1.0")
        classpath("dev.icerock.moko:resources-generator:0.16.1")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }

    // Workaround for https://youtrack.jetbrains.com/issue/KT-36721.
    pluginManager.withPlugin("kotlin-multiplatform") {
        val kotlinExtension = project.extensions.getByName("kotlin")
                as org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
        val uniqueName = "${project.group}.${project.name}"

        kotlinExtension.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
            compilations["main"].kotlinOptions.freeCompilerArgs += listOf(
                "-module-name",
                uniqueName
            )
        }
    }

    configurations
        .matching { it.name == "compileOnly" }
        .configureEach {
            dependencies {
                // fix of package javax.annotation does not exist import javax.annotation.Generated in DataBinding code
                "compileOnly"("javax.annotation:jsr250-api:1.0")
            }
        }

    val project = this
    val bintrayPath: Pair<String, String>?
    when {
        this.name.startsWith("widgets") -> {
            bintrayPath = "moko" to "moko-widgets"

            this.group = "dev.icerock.moko"
            this.version = Versions.Libs.MultiPlatform.mokoWidgets

            this.plugins.withType<com.android.build.gradle.LibraryPlugin> {
                this@allprojects.configure<com.android.build.gradle.LibraryExtension> {
                    compileSdkVersion(Versions.Android.compileSdk)

                    defaultConfig {
                        minSdkVersion(Versions.Android.minSdk)
                        targetSdkVersion(Versions.Android.targetSdk)
                    }
                }
            }
        }
        else -> {
            bintrayPath = null
        }
    }

    if (bintrayPath != null) {
        project.plugins.withType<MavenPublishPlugin> {
            project.configure<PublishingExtension> {
                val repo = bintrayPath.first
                val artifact = bintrayPath.second
                val isDevPublish = project.properties.containsKey("devPublish")
                val fullRepoName = if (isDevPublish) "$repo-dev" else repo
                val mavenUrl =
                    "https://api.bintray.com/maven/icerockdev/$fullRepoName/$artifact/;publish=1"

                repositories.maven(mavenUrl) {
                    this.name = "bintray"

                    credentials {
                        username = System.getProperty("BINTRAY_USER")
                        password = System.getProperty("BINTRAY_KEY")
                    }
                }
            }
        }

        apply<dev.icerock.moko.widgets.gradle.BintrayPublishingPlugin>()
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
