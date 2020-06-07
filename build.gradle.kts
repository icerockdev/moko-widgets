/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

buildscript {
    repositories {
        mavenLocal()

        jcenter()
        google()

        maven { url = uri("https://dl.bintray.com/kotlin/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }

        maven { url = uri("https://dl.bintray.com/icerockdev/plugins-dev") }
    }
    if (!properties.containsKey("pluginPublish")) {
        dependencies {
            Deps.plugins.values.forEach { classpath(it) }
        }
    }
}

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

    // Workaround for https://youtrack.jetbrains.com/issue/KT-36721.
    pluginManager.withPlugin("kotlin-multiplatform") {
        val kotlinExtension = project.extensions.getByName("kotlin")
                as org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
        val uniqueName = "${project.group}.${project.name}"

        kotlinExtension.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
            compilations["main"].kotlinOptions.freeCompilerArgs += listOf("-module-name", uniqueName)
        }
    }

    afterEvaluate {
        dependencies {
            if (configurations.map { it.name }.contains("compileOnly")) {
                // fix of package javax.annotation does not exist import javax.annotation.Generated in DataBinding code
                "compileOnly"("javax.annotation:jsr250-api:1.0")
            }
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
        this.name.endsWith("-plugin") -> {
            bintrayPath = "plugins" to "moko-widgets-generator"

            this.group = "dev.icerock.moko.widgets"
            this.version = Versions.Plugins.mokoWidgets

            this.plugins.withType<JavaPlugin> {
                this@allprojects.configure<JavaPluginExtension> {
                    sourceCompatibility = JavaVersion.VERSION_1_6
                    targetCompatibility = JavaVersion.VERSION_1_6
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

        project.afterEvaluate {
            val publishNoValidationTasks = tasks.filterIsInstance<PublishToMavenRepository>()
                .map { publishTask ->
                    val newName = publishTask.name.replace("publish", "publishNoValidation")
                    val newTask = tasks.create(newName, PublishWithoutValidationToMavenRepository::class)

                    newTask.group = PublishingPlugin.PUBLISH_TASK_GROUP + " no validation"
                    newTask.publication = publishTask.publication
                    newTask.repository = publishTask.repository
                    newTask.setDependsOn(publishTask.dependsOn)

                    newTask
                }

            publishNoValidationTasks
                .groupBy { it.repository }
                .forEach { (repo, publishTasks) ->
                    tasks.create("publishNoValidationTo${repo.name.capitalize()}") {
                        group = PublishingPlugin.PUBLISH_TASK_GROUP + " no validation"
                        setDependsOn(publishTasks)
                    }
                }
        }
    }
}

open class PublishWithoutValidationToMavenRepository : PublishToMavenRepository() {
    override fun publish() {
        val remotePublisher =
            org.gradle.api.publish.maven.internal.publisher.BintrayPublisher(temporaryDirFactory)
        val normalizedPublication = publicationInternal.asNormalisedPublication()
        remotePublisher.publish(normalizedPublication, repository)
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
