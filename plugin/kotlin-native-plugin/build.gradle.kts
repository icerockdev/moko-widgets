/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
    id("kotlin-kapt")
}

val embedImplementationConfig = "embedImplementation"
configurations {
    val embedImplementation = create(embedImplementationConfig)
    implementation.get().extendsFrom(embedImplementation)
}

dependencies {
    implementation(Deps.Libs.Jvm.kotlinStdLib)

    embedImplementationConfig(project(":kotlin-common-plugin"))

    compileOnly("org.jetbrains.kotlin:kotlin-compiler")

    compileOnly(Deps.Libs.Jvm.autoService)
    kapt(Deps.Libs.Jvm.autoService)
}

tasks.jar {
    from({
        val embedConfiguration = configurations.getByName(embedImplementationConfig)
        embedConfiguration.map { if(it.isDirectory) it else zipTree(it) }
    })
}

publishing {
    publications {
        register("pluginMaven", MavenPublication::class) {
            from(components["java"])
        }
    }
}
