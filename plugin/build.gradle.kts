/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

allprojects {
    repositories {
        google()
        jcenter()

        maven { url = uri("https://kotlin.bintray.com/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://dl.bintray.com/icerockdev/moko") }
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }
    }

    val bintrayPath = "plugins" to "moko-widgets-generator"
    val project = this

    project.group = "dev.icerock.moko.widgets"
    project.version = Versions.Plugins.mokoWidgets

    project.plugins.withType<JavaPlugin> {
        project.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_6
            targetCompatibility = JavaVersion.VERSION_1_6
        }
    }

    project.plugins.withType<MavenPublishPlugin> {
        project.configure<PublishingExtension> {
            val repo = bintrayPath.first
            val artifact = bintrayPath.second
            val isDevPublish = project.properties.containsKey("devPublish")
            val fullRepoName = if (isDevPublish) "$repo-dev" else repo
            val mavenUrl = "https://api.bintray.com/maven/icerockdev/$fullRepoName/$artifact/;publish=1"

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

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
