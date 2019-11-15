/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.resources.desc.StringDesc
import kotlin.reflect.KClass

expect abstract class NavigationScreen<S>(
    screenFactory: ScreenFactory
) : Screen<Args.Empty> where S : Screen<Args.Empty>, S : NavigationItem {
    abstract val rootScreen: KClass<out S>

    fun <S> routeToScreen(screen: KClass<out S>) where S : Screen<Args.Empty>, S : NavigationItem
    fun <A : Parcelable, S> routeToScreen(
        screen: KClass<out S>,
        args: A
    ) where S : Screen<Args.Parcel<A>>, S : NavigationItem
}

interface NavigationItem {
    val navigationTitle: StringDesc
}
