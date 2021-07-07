/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    input.setFrom("src/commonMain/kotlin", "src/androidMain/kotlin", "src/iosMain/kotlin", "src/main/kotlin")
}

dependencies {
    "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.15.0")
}
