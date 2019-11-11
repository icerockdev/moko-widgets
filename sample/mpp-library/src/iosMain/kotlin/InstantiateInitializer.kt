/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.icerockdev.library.sample.CartNavigationScreen
import com.icerockdev.library.sample.CartScreen
import com.icerockdev.library.sample.MyBottomNavigationScreen
import com.icerockdev.library.sample.ProductScreen
import com.icerockdev.library.sample.ProductsNavigationScreen
import com.icerockdev.library.sample.ProductsScreen
import com.icerockdev.library.sample.WidgetsScreen
import dev.icerock.moko.widgets.screen.registerScreenFactory

private val instantiateInitializerHack: Int = {
    registerScreenFactory(MyBottomNavigationScreen::class) { MyBottomNavigationScreen() }
    registerScreenFactory(ProductsNavigationScreen::class) { ProductsNavigationScreen() }
    registerScreenFactory(CartNavigationScreen::class) { CartNavigationScreen() }
    registerScreenFactory(WidgetsScreen::class) { WidgetsScreen() }
    registerScreenFactory(ProductsScreen::class) { ProductsScreen() }
    registerScreenFactory(CartScreen::class) { CartScreen() }
    registerScreenFactory(ProductScreen::class) { ProductScreen() }
    
    println("factories registered")

    1
}.invoke()