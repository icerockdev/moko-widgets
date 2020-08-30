/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    plugin(Deps.Plugins.kotlinJvm)
    plugin(Deps.Plugins.mavenPublish)
    plugin(Deps.Plugins.kotlinKapt)
}

val embedImplementationConfig = "embedImplementation"
configurations {
    val embedImplementation = create(embedImplementationConfig)
    implementation.get().extendsFrom(embedImplementation)
}

dependencies {
    embedImplementationConfig(project(":kotlin-common-plugin"))

    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

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
