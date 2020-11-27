/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-script-runtime")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.jetbrains.intellij.deps:trove4j:1.0.20181211")
}
