/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

actual abstract class NavigationScreen actual constructor(
    private val screenFactory: ScreenFactory
) : Screen<Args.Empty>() {

    actual abstract val rootScreen: KClass<out Screen<Args.Empty>>

    override fun createViewController(): UIViewController {
        return NavigationViewController(this, screenFactory).apply {
            this@NavigationScreen.navigation = this.Nav()
        }
    }

    actual fun routeToScreen(screen: KClass<out Screen<Args.Empty>>) {
        navigation?.routeToScreen(screen)
    }

    actual fun <T : Parcelable> routeToScreen(
        screen: KClass<out Screen<Args.Parcel<T>>>,
        args: T
    ) {
        navigation?.routeToScreen(argument = args, screen = screen)
    }
}
