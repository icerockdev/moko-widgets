/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.NavigationScreen
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.ScreenFactory
import kotlin.reflect.KClass

class ProductsNavigationScreen(screenFactory: ScreenFactory) : NavigationScreen(screenFactory) {
    override val rootScreen: KClass<out Screen<Args.Empty>>
        get() = ProductsScreen::class
}