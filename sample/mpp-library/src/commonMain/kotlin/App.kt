/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.icerockdev.library.AppTheme
import com.icerockdev.library.SharedFactory
import com.icerockdev.library.universal.CartNavigationScreen
import com.icerockdev.library.universal.CartScreen
import com.icerockdev.library.universal.LoginScreen
import com.icerockdev.library.universal.LoginViewModel
import com.icerockdev.library.universal.ProductScreen
import com.icerockdev.library.universal.ProductsNavigationScreen
import com.icerockdev.library.universal.ProductsScreen
import com.icerockdev.library.universal.ProfileScreen
import com.icerockdev.library.universal.RootBottomNavigationScreen
import com.icerockdev.library.universal.WidgetsScreen
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.flat.FlatInputViewFactory
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.NavigationScreen
import dev.icerock.moko.widgets.screen.PlatformScreen
import dev.icerock.moko.widgets.screen.RootNavigationScreen
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.ScreenFactory
import dev.icerock.moko.widgets.screen.rootNavigationScreen
import dev.icerock.moko.widgets.style.view.TextStyle
import kotlin.reflect.KClass

class ScreensPlatformDeps(
    val profileScreenDeps: PlatformScreen.Deps<Args.Parcel<ProfileScreen.Arg>, ProfileScreen.Bundle>
)

class App(
    private val widgetsPlatformDeps: WidgetsPlatformDeps,
    private val screensPlatformDeps: ScreensPlatformDeps
) : BaseApplication() {
    interface WidgetsPlatformDeps : FlatInputViewFactory.PlatformDependency

    override fun setup() {
        val sharedFactory = SharedFactory()
        val theme = AppTheme.baseTheme

        val loginTheme = Theme(AppTheme.loginScreen) {
            factory[InputWidget.DefaultCategory] = FlatInputViewFactory(
                platformDependency = widgetsPlatformDeps,
                textStyle = TextStyle(
                    size = 16,
                    color = Color(0x16171AFF)
                ),
                backgroundColor = Color(0xF5F5F5FF)
            )
        }

        registerScreenFactory(RootBottomNavigationScreen::class) { RootBottomNavigationScreen(this) }
        registerScreenFactory(ProductsNavigationScreen::class) { ProductsNavigationScreen(this) }
        registerScreenFactory(CartNavigationScreen::class) { CartNavigationScreen(this) }
        registerScreenFactory(WidgetsScreen::class) { WidgetsScreen(sharedFactory, theme, AppTheme.PostsCollection) }
        registerScreenFactory(ProductsScreen::class) { ProductsScreen(theme) }
        registerScreenFactory(CartScreen::class) { CartScreen(theme) }
        registerScreenFactory(ProductScreen::class) { ProductScreen(theme) }

        registerScreenFactory(LoginScreen::class) { LoginScreen(loginTheme) { LoginViewModel(it) } }

        registerScreenFactory(ProfileScreen::class) { ProfileScreen(screensPlatformDeps.profileScreenDeps) }

        registerScreenFactory(MainNavigationScreen::class) { MainNavigationScreen(this) }
    }

    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return MainNavigationScreen::class
    }
}

class MainNavigationScreen(
    screenFactory: ScreenFactory
) : NavigationScreen(screenFactory), LoginScreen.Parent {
    override val rootScreen: RootNavigationScreen = LoginScreen::class.rootNavigationScreen()

    override fun routeToMain() {
        routeToScreen(ProfileScreen::class, ProfileScreen.Arg(userId = 9))
    }
}
