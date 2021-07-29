/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
}

val embedImplementationConfig = "embedImplementation"

configurations {
    val embedImplementation = create(embedImplementationConfig)
    implementation.get().extendsFrom(embedImplementation)
}

tasks.jar {
    from({
        val embedConfiguration = configurations.getByName(embedImplementationConfig)
        embedConfiguration.map { file ->
            if (file.isDirectory) file else zipTree(file).matching {
                exclude("META-INF/versions/**")
            }
        }
    })
}