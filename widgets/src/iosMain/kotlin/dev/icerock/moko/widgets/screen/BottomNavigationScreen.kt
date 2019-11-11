/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

actual abstract class BottomNavigationScreen actual constructor() : Screen<Args.Empty>(), Navigation {
    actual abstract val items: List<BottomNavigationItem>

    override fun createViewController(): UIViewController {
        return BottomNavigationViewController(this).apply {
            this@BottomNavigationScreen.navigation = this.Nav()
        }
    }

    override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
        navigation?.routeToScreen(screen = screen)
    }

    override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
        navigation?.routeToScreen(argument = argument, screen = screen)
    }
}

