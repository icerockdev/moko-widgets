/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

expect abstract class PlatformScreen<A : Args, T>(
    deps: Deps<A, T>
) : Screen<A> {
    abstract fun createPlatformBundle(): T

    interface Deps<A : Args, T>
}
