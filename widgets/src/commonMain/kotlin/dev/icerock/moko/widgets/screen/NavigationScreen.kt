/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.resources.desc.StringDesc
import kotlin.reflect.KClass

expect abstract class NavigationScreen(
    screenFactory: ScreenFactory
) : Screen<Args.Empty> {
    abstract val rootScreen: RootNavigationScreen

    fun <S> routeToScreen(
        screen: KClass<out S>
    ) where S : Screen<Args.Empty>, S : NavigationItem

    fun <A : Parcelable, S> routeToScreen(
        screen: KClass<out S>,
        args: A
    ) where S : Screen<Args.Parcel<A>>, S : NavigationItem

    fun <S> setScreen(
        screen: KClass<out S>
    ) where S : Screen<Args.Empty>, S : NavigationItem

    fun <A : Parcelable, S> setScreen(
        screen: KClass<out S>,
        args: A
    ) where S : Screen<Args.Parcel<A>>, S : NavigationItem
}

@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class RootNavigationScreen private constructor(
    val screenClass: KClass<out Screen<Args.Empty>>
) {
    companion object {
        fun <S> from(
            screenClass: KClass<out S>
        ): RootNavigationScreen where S : Screen<Args.Empty>, S : NavigationItem {
            return RootNavigationScreen(screenClass)
        }
    }
}

fun <S> KClass<out S>.rootNavigationScreen(
): RootNavigationScreen where S : Screen<Args.Empty>, S : NavigationItem {
    return RootNavigationScreen.from(this)
}

interface NavigationItem {
    val navigationBar: NavigationBar
}

sealed class NavigationBar {
    object None : NavigationBar()
    data class Normal(val title: StringDesc) : NavigationBar()
}
