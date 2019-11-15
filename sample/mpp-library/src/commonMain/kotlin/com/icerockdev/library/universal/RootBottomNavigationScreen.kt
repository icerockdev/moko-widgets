/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.screen.BottomNavigationItem
import dev.icerock.moko.widgets.screen.BottomNavigationScreen
import dev.icerock.moko.widgets.screen.ScreenFactory

class RootBottomNavigationScreen(screenFactory: ScreenFactory) : BottomNavigationScreen(screenFactory),
    ProductsNavigationScreen.Parent {
    override val items: List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            id = 1,
            title = "Products".desc(),
            screen = ProductsNavigationScreen::class
        ),
        BottomNavigationItem(
            id = 2,
            title = "Cart".desc(),
            screen = CartNavigationScreen::class
        ),
        BottomNavigationItem(
            id = 3,
            title = "Widgets".desc(),
            screen = WidgetsScreen::class
        )
    )

    override fun routeToCart() {
        selectedItemId = 2
    }
}