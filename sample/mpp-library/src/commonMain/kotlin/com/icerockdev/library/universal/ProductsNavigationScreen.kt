/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.widgets.screen.NavigationScreen
import dev.icerock.moko.widgets.screen.ScreenFactory
import dev.icerock.moko.widgets.screen.getParentScreen
import kotlin.reflect.KClass

class ProductsNavigationScreen(
    screenFactory: ScreenFactory
) : NavigationScreen<ProductsScreen>(screenFactory), ProductsScreen.Parent, ProductScreen.Parent {
    override val rootScreen: KClass<out ProductsScreen>
        get() = ProductsScreen::class

    override fun routeToProduct(productId: Int) {
        routeToScreen(
            ProductScreen::class,
            ProductScreen.Args(10)
        )
    }

    override fun routeToCart() {
        getParentScreen<Parent>().routeToCart()
    }

    interface Parent {
        fun routeToCart()
    }
}