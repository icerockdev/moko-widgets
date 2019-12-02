/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.icerockdev.library.SharedFactory
import com.icerockdev.library.sample.CryptoProfileScreen
import com.icerockdev.library.sample.UsersScreen
import com.icerockdev.library.universal.CartNavigationScreen
import com.icerockdev.library.universal.CartScreen
import com.icerockdev.library.universal.LoginScreen
import com.icerockdev.library.universal.ProductScreen
import com.icerockdev.library.universal.ProductsNavigationScreen
import com.icerockdev.library.universal.ProductsScreen
import com.icerockdev.library.universal.RootBottomNavigationScreen
import com.icerockdev.library.universal.WidgetsScreen
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.factory.DefaultListWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultListWidgetViewFactoryBase
import dev.icerock.moko.widgets.factory.DefaultTextWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultTextWidgetViewFactoryBase
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.setListFactory
import dev.icerock.moko.widgets.setTextFactory
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextAlignment
import kotlin.native.concurrent.ThreadLocal
import kotlin.reflect.KClass

@ThreadLocal
object App : BaseApplication() {
    override fun setup() {
        val sharedFactory = SharedFactory()
        val theme = Theme {
            setTextFactory(
                DefaultTextWidgetViewFactory(
                    DefaultTextWidgetViewFactoryBase.Style(
                        textAlignment = TextAlignment.CENTER
                    )
                ), CryptoProfileScreen.Id.DelimiterText
            )

            setListFactory(
                DefaultListWidgetViewFactory(
                    DefaultListWidgetViewFactoryBase.Style(
                        padding = PaddingValues(8f)
                    )
                ), UsersScreen.Id.List
            )
        }

        registerScreenFactory(RootBottomNavigationScreen::class) { RootBottomNavigationScreen(this) }
        registerScreenFactory(ProductsNavigationScreen::class) { ProductsNavigationScreen(this) }
        registerScreenFactory(CartNavigationScreen::class) { CartNavigationScreen(this) }
        registerScreenFactory(WidgetsScreen::class) { WidgetsScreen(sharedFactory, theme) }
        registerScreenFactory(ProductsScreen::class) { ProductsScreen(theme) }
        registerScreenFactory(CartScreen::class) { CartScreen(theme) }
        registerScreenFactory(ProductScreen::class) { ProductScreen(theme) }

        registerScreenFactory(LoginScreen::class) { LoginScreen(theme) }
    }

    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return LoginScreen::class
    }
}
