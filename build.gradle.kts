/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

buildscript {
    repositories {
        mavenLocal()

        jcenter()
        google()

        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") }

        maven { url = uri("https://dl.bintray.com/icerockdev/plugins-dev") }
    }
    if (!properties.containsKey("pluginPublish")) {
        dependencies {
            classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
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
    val bintrayRepository: Pair<String, String>?
    when {
        this.name.startsWith("widgets") -> {
            apply(plugin = "com.jfrog.bintray")

            bintrayRepository = "moko" to "moko-widgets"

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
            apply(plugin = "com.jfrog.bintray")

            bintrayRepository = "plugins" to "moko-widgets-generator"

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
            bintrayRepository = null
        }
    }

    fun setupBintray(repo: String, name: String, publications: List<MavenPublication>) {
        project.plugins.withType<com.jfrog.bintray.gradle.BintrayPlugin> {
            project.configure<com.jfrog.bintray.gradle.BintrayExtension> {
                user = System.getProperty("BINTRAY_USER")
                key = System.getProperty("BINTRAY_KEY")
                isPublish = true

                setPublications(*publications.map { it.name }.toTypedArray())

                val devPublish = project.properties.containsKey("devPublish")
                pkg = PackageConfig()
                pkg.repo = repo + (if (devPublish) "-dev" else "")
                pkg.name = name
                pkg.userOrg = "icerockdev"

                isOverride = devPublish
            }
        }

        tasks.withType<com.jfrog.bintray.gradle.tasks.BintrayUploadTask> {
            doFirst {
                publications.forEach { publication ->
                    val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
                    if (moduleFile.exists()) {
                        publication.artifact(object :
                            org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact(moduleFile) {
                            override fun getDefaultExtension() = "module"
                        })
                    }
                }
            }
        }
    }

    if (bintrayRepository != null) {
        project.plugins.withType<MavenPublishPlugin> {
            project.afterEvaluate {
                val mavenPublications = project.extensions
                    .getByType<PublishingExtension>()
                    .publications
                    .filterIsInstance<MavenPublication>()

                setupBintray(
                    repo = bintrayRepository.first,
                    name = bintrayRepository.second,
                    publications = mavenPublications
                )
            }
        }
    }
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.buildDir)
}
