/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import kotlin.reflect.KClass

expect abstract class NavigationScreen(
    screenFactory: ScreenFactory
) : Screen<Args.Empty> {
    abstract val rootScreen: KClass<out Screen<Args.Empty>>

    fun routeToScreen(screen: KClass<out Screen<Args.Empty>>)
    fun <T : Parcelable> routeToScreen(screen: KClass<out Screen<Args.Parcel<T>>>, args: T)
}