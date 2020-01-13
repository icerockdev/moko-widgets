/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.icerockdev.library.AppTheme
import com.icerockdev.library.MR
import com.icerockdev.library.SharedFactory
import com.icerockdev.library.universal.CartScreen
import com.icerockdev.library.universal.LoginScreen
import com.icerockdev.library.universal.LoginViewModel
import com.icerockdev.library.universal.PlatformProfileScreen
import com.icerockdev.library.universal.ProductScreen
import com.icerockdev.library.universal.ProductsScreen
import com.icerockdev.library.universal.WidgetsScreen
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.flat.FlatInputViewFactory
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.ScreenDesc
import dev.icerock.moko.widgets.screen.navigation.BottomNavigationScreen
import dev.icerock.moko.widgets.screen.navigation.NavigationScreen
import dev.icerock.moko.widgets.screen.navigation.createPushRoute
import dev.icerock.moko.widgets.screen.navigation.createRouter
import dev.icerock.moko.widgets.style.view.TextStyle

interface WidgetsPlatformDeps : FlatInputViewFactory.PlatformDependency
interface ScreensPlatformDeps : PlatformProfileScreen.Deps

class App(
    private val widgetsPlatformDeps: WidgetsPlatformDeps,
    private val screensPlatformDeps: ScreensPlatformDeps
) : BaseApplication() {

    override fun setup(): ScreenDesc<Args.Empty> {
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

        val mainScreen = registerScreen(BottomNavigationScreen::class) {
            val bottomRouter = Unit // createRouter()

            val cartNavigation = registerScreen(NavigationScreen::class) {
                val cartScreen = registerScreen(CartScreen::class) {
                    CartScreen(theme)
                }
                NavigationScreen(
                    initialScreen = cartScreen,
                    router = createRouter()
                )
            }

            val productsNavigation = registerScreen(NavigationScreen::class) {
                val productScreen = registerScreen(ProductScreen::class) {
                    ProductScreen(
                        theme = theme,
                        cartRoute = TODO() // bottomRouter.createChangeTabRoute(cartNavigation)
                    )
                }
                val router = createRouter()
                val productsScreen = registerScreen(ProductsScreen::class) {
                    ProductsScreen(
                        theme = theme,
                        productRoute = router.createPushRoute(productScreen) {
                            ProductScreen.Args(productId = it)
                        }
                    )
                }
                NavigationScreen(
                    initialScreen = productsScreen,
                    router = router
                )
            }

            val widgetsScreen = registerScreen(WidgetsScreen::class) {
                WidgetsScreen(sharedFactory, theme, AppTheme.PostsCollection)
            }

            BottomNavigationScreen {
                tab(
                    id = 1,
                    title = "Products".desc(),
                    icon = MR.images.home_black_18,
                    screenDesc = productsNavigation
                )
                tab(
                    id = 2,
                    title = "Cart".desc(),
                    icon = MR.images.cart_black_18,
                    screenDesc = cartNavigation
                )
                tab(
                    id = 3,
                    title = "Widgets".desc(),
                    icon = MR.images.stars_black_18,
                    screenDesc = widgetsScreen
                )
            }
        }

        val profileScreen = registerScreen(PlatformProfileScreen::class) {
            PlatformProfileScreen(screensPlatformDeps)
        }

        return registerScreen(NavigationScreen::class) {
            val router = createRouter()

            val loginScreen = registerScreen(LoginScreen::class) {
                LoginScreen(
                    theme = loginTheme,
                    mainRoute = TODO() // router.createReplaceRoute(mainScreen)
                ) { LoginViewModel(it) }
            }

            NavigationScreen(
                initialScreen = loginScreen,
                router = createRouter()
            )
        }
    }
}
