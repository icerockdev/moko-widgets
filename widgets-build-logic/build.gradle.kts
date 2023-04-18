plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()

    mavenCentral()
    google()
}

dependencies {
    api("dev.icerock:mobile-multiplatform:0.14.2")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    api("com.android.tools.build:gradle:7.4.2")
    api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0")
}
