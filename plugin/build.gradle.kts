/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.5.20")
    id("com.gradle.plugin-publish") version ("0.15.0")
    id("detekt-convention")
    id("publication-convention")
    id("java-gradle-plugin")
}

buildscript {
    repositories {
        mavenCentral()
        google()

        gradlePluginPortal()
        jcenter()
    }
    dependencies {
        classpath(":widgets-build-logic")
    }
}

allprojects {
    group = "dev.icerock.moko.widgets"
    version = rootProject.libs.versions.mokoWidgetsVersion.get()

    project.plugins.withType<JavaPlugin> {
        project.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}
publishing.publications.register("mavenJava", MavenPublication::class) {
    from(components["java"])
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

gradlePlugin {
    plugins {
        create("multiplatform-widgets-generator") {
            id = "dev.icerock.mobile.multiplatform-widgets-generator"
            implementationClass = "dev.icerock.moko.widgets.WidgetsGeneratorGradlePlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/icerockdev/moko-widgets/tree/master/plugin"
    vcsUrl = "https://github.com/icerockdev/moko-widgets/tree/master/plugin"
    description = "Plugin to codegen for new Widgets"
    tags = listOf("multiplatform-widgets-generator")

    plugins {
        getByName("multiplatform-widgets-generator") {
            displayName = "Widgets Generator for IceRock projects"
        }
    }

    mavenCoordinates {
        groupId = project.group as String
        artifactId = project.name
        version = project.version as String
    }
}
