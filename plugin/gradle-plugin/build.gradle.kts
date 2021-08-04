/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.gradle.maven-publish")
    id("kotlin-kapt")
    id("com.github.gmazzo.buildconfig") version ("3.0.2")
    
    id("com.gradle.plugin-publish")
    id("detekt-convention")
    id("publication-convention")
    id("java-gradle-plugin")
    
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    compileOnly(libs.kotlinGradlePlugin)
    implementation(libs.kotlinGradlePluginApi)

    compileOnly(libs.autoService)
    kapt(libs.autoService)
}

buildConfig {
    sourceSets.getByName("main") {
        buildConfig {
            buildConfigField("String", "compilerPluginVersion", "\"${project.version}\"")
        }
    }
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            from(components["java"])
        }
    }
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
    website = "https://github.com/icerockdev/moko-widgets"
    vcsUrl = "https://github.com/icerockdev/moko-widgets"
    description = "Plugin to codegen for new Widgets"
    tags = listOf("moko-widgets", "moko", "kotlin", "kotlin-multiplatform")

    plugins {
        getByName("multiplatform-widgets-generator") {
            displayName = "MOKO Widgets generator plugin"
        }
    }

    mavenCoordinates {
        groupId = project.group as String
        artifactId = project.name
        version = project.version as String
    }
}
