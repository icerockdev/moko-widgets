/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.widgets.screen.NavigationScreen
import dev.icerock.moko.widgets.screen.RootNavigationScreen
import dev.icerock.moko.widgets.screen.ScreenFactory

class CartNavigationScreen(
    screenFactory: ScreenFactory
) : NavigationScreen(screenFactory) {
    override val rootScreen = RootNavigationScreen.from(CartScreen::class)
}
