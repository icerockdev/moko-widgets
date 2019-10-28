/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

object Deps {
    object Libs {
        object Android {
            val kotlinStdLib = AndroidLibrary(
                name = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
            )
            val appCompat = AndroidLibrary(
                name = "androidx.appcompat:appcompat:${Versions.Libs.Android.appCompat}"
            )
            val recyclerView = AndroidLibrary(
                name = "androidx.recyclerview:recyclerview:${Versions.Libs.Android.recyclerView}"
            )
        }

        object MultiPlatform {
            val kotlinStdLib = MultiPlatformLibrary(
                android = Android.kotlinStdLib.name,
                common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
            )
            val mokoWidgets = MultiPlatformLibrary(
                common = "dev.icerock.moko:widgets:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosX64 = "dev.icerock.moko:widgets-iosx64:${Versions.Libs.MultiPlatform.mokoWidgets}",
                iosArm64 = "dev.icerock.moko:widgets-iosarm64:${Versions.Libs.MultiPlatform.mokoWidgets}"
            )
        }
    }
}