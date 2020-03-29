/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

allprojects {
    repositories {
        mavenLocal()

        google()
        jcenter()

        maven { url = uri("https://kotlin.bintray.com/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://dl.bintray.com/icerockdev/moko") }
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
        maven { url = uri("http://dl.bintray.com/lukaville/maven") }

        maven { url = uri("https://dl.bintray.com/icerockdev/moko-dev") }
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins-dev") }
    }

    afterEvaluate {
        dependencies {
            if (configurations.map { it.name }.contains("compileOnly")) {
                // fix of package javax.annotation does not exist import javax.annotation.Generated in DataBinding code
                "compileOnly"("javax.annotation:jsr250-api:1.0")
            }
        }
    }

    val devPublishing: Boolean = properties.containsKey("devPublish")

    if (this.name.startsWith("widgets")) {
        this.group = "dev.icerock.moko"
        this.version = when (devPublishing) {
            true -> getGitCommit()
            false -> Versions.Libs.MultiPlatform.mokoWidgets
        }

        this.plugins.withType<MavenPublishPlugin> {
            this@allprojects.configure<PublishingExtension> {
                registerBintrayMaven(
                    name = "bintray",
                    url = "https://api.bintray.com/maven/icerockdev/moko/moko-widgets/;publish=1"
                )
                registerBintrayMaven(
                    name = "bintrayDev",
                    url = "https://api.bintray.com/maven/icerockdev/moko-dev/moko-widgets/;publish=1"
                )
            }
        }

        this.plugins.withType<com.android.build.gradle.LibraryPlugin> {
            this@allprojects.configure<com.android.build.gradle.LibraryExtension> {
                compileSdkVersion(Versions.Android.compileSdk)

                defaultConfig {
                    minSdkVersion(Versions.Android.minSdk)
                    targetSdkVersion(Versions.Android.targetSdk)
                }
            }
        }
    } else if (this.name.endsWith("-plugin")) {
        this.group = "dev.icerock.moko.widgets"
        this.version = Versions.Plugins.mokoWidgets

        this.plugins.withType<MavenPublishPlugin> {
            this@allprojects.configure<PublishingExtension> {
                registerBintrayMaven(
                    name = "bintray",
                    url = "https://api.bintray.com/maven/icerockdev/plugins/moko-widgets-generator/;publish=1"
                )
                registerBintrayMaven(
                    name = "bintrayDev",
                    url = "https://api.bintray.com/maven/icerockdev/plugins-dev/moko-widgets-generator/;publish=1"
                )
            }
        }

        this.plugins.withType<JavaPlugin> {
            this@allprojects.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_1_6
                targetCompatibility = JavaVersion.VERSION_1_6
            }
        }
    }
}

fun PublishingExtension.registerBintrayMaven(name: String, url: String) {
    repositories.maven(url) {
        this.name = name

        credentials {
            username = System.getProperty("BINTRAY_USER")
            password = System.getProperty("BINTRAY_KEY")
        }
    }
}

fun getGitCommit(): String {
    val stdout = java.io.ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
