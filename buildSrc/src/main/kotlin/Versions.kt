/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

object Versions {
    object Android {
        const val compileSdk = 28
        const val targetSdk = 28
        const val minSdk = 16
    }

    const val kotlin = "1.3.61"
    private const val mokoWidgets = "0.1.0-dev-6"
    private const val mokoResources = "0.6.0-dev-1"

    object Plugins {
        const val mokoWidgets = Versions.mokoWidgets
        const val mokoResources = Versions.mokoResources
    }

    object Libs {
        object Android {
            const val appCompat = "1.1.0"
            const val swipeRefreshLayout = "1.0.0"
            const val material = "1.0.0"
            const val constraintLayout = "1.1.3"
            const val lifecycle = "2.0.0"
            const val recyclerView = "1.0.0"
            const val inputMask = "5.0.0"
            const val glide = "4.10.0"
        }

        object MultiPlatform {
            const val coroutines = "1.3.2-1.3.60"

            const val mokoWidgets = Versions.mokoWidgets
            const val mokoResources = Versions.mokoResources
            const val mokoMvvm = "0.4.0-dev-2"
            const val mokoFields = "0.2.0-dev-1"
            const val mokoUnits = "0.2.0-dev-2"
            const val mokoMedia = "0.2.0-dev-1"
            const val mokoGraphics = "0.2.0"
            const val mokoParcelize = "0.2.0"
        }
    }
}
