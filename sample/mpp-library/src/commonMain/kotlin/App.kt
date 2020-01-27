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
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.flat.FlatInputViewFactory
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.ScreenDesc
import dev.icerock.moko.widgets.screen.TypedScreenDesc
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.navigation.BottomNavigationItem
import dev.icerock.moko.widgets.screen.navigation.BottomNavigationScreen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.screen.navigation.NavigationScreen
import dev.icerock.moko.widgets.screen.navigation.Resultable
import dev.icerock.moko.widgets.screen.navigation.createPushResultRoute
import dev.icerock.moko.widgets.screen.navigation.createPushRoute
import dev.icerock.moko.widgets.screen.navigation.createReplaceRoute
import dev.icerock.moko.widgets.screen.navigation.createRouter
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

interface ScreensPlatformDeps : PlatformProfileScreen.Deps

class App(
    private val screensPlatformDeps: ScreensPlatformDeps
) : BaseApplication() {

    override fun setup(): ScreenDesc<Args.Empty> {
        val sharedFactory = SharedFactory()
        val theme = AppTheme.baseTheme

        val loginTheme = Theme(AppTheme.loginScreen) {
            factory[LoginScreen.Id.EmailInputId] = FlatInputViewFactory(
                textStyle = TextStyle(
                    size = 16,
                    color = Color(0x16171AFF)
                ),
                backgroundColor = Color(0xF5F5F5FF)
            )
        }

        val mainScreen = registerScreen(MainBottomNavigationScreen::class) {
            val bottomRouter = createRouter()

            val cartNavigation = registerScreen(NavigationScreen::class) {
                val cartScreen = registerScreen(CartScreen::class) {
                    CartScreen(theme)
                }
                CartNavigationScreen(
                    initialScreen = cartScreen,
                    router = createRouter()
                )
            }

            val productsNavigation = registerScreen(NavigationScreen::class) {
                val navigationRouter = createRouter()

                val productScreen = registerScreen(ProductScreen::class) {
                    ProductScreen(
                        theme = theme,
                        cartRoute = bottomRouter.createChangeTabRoute(2)
                    )
                }
                val productsScreen = registerScreen(ProductsScreen::class) {
                    ProductsScreen(
                        theme = theme,
                        productRoute = navigationRouter.createPushRoute(productScreen) {
                            ProductScreen.Args(productId = it)
                        }
                    )
                }
                ProductsNavigationScreen(
                    initialScreen = productsScreen,
                    router = navigationRouter
                )
            }

            val widgetsScreen = registerScreen(WidgetsScreen::class) {
                WidgetsScreen(sharedFactory, theme, AppTheme.PostsCollection)
            }

            MainBottomNavigationScreen(bottomRouter) {
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

            val regScreen = registerScreen(RegisterScreen::class) {
                RegisterScreen(theme)
            }

            val loginScreen = registerScreen(LoginScreen::class) {
                LoginScreen(
                    theme = loginTheme,
                    mainRoute = router.createReplaceRoute(mainScreen),
                    registerRoute = router.createPushResultRoute(regScreen) { it.token }
                ) { LoginViewModel(it) }
            }

            RootNavigationScreen(
                initialScreen = loginScreen,
                router = router
            )
        }
    }
}

class RegisterScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), Resultable<RegisterScreen.Result>, NavigationItem {
    override val navigationBar: NavigationBar = NavigationBar.Normal(title = "Registration".desc())

    override var screenResult: Result? = null

    override fun createContentWidget() = with(theme) {
        container(size = WidgetSize.AsParent) {
            center {
                button(
                    size = WidgetSize.WrapContent,
                    content = ButtonWidget.Content.Text(Value.data("set result".desc()))
                ) {
                    screenResult = Result("my result")
                }
            }
        }
    }

    @Parcelize
    data class Result(val token: String) : Parcelable
}

// TODO required for Android side... should be reworked if any ideas will be
class MainBottomNavigationScreen(
    router: Router,
    builder: BottomNavigationItem.Builder.() -> Unit
) : BottomNavigationScreen(router, builder), NavigationItem {
    override val navigationBar: NavigationBar = NavigationBar.None
}

class RootNavigationScreen(initialScreen: TypedScreenDesc<Args.Empty, LoginScreen>, router: Router) :
    NavigationScreen<LoginScreen>(initialScreen, router)

class ProductsNavigationScreen(initialScreen: TypedScreenDesc<Args.Empty, ProductsScreen>, router: Router) :
    NavigationScreen<ProductsScreen>(initialScreen, router)

class CartNavigationScreen(initialScreen: TypedScreenDesc<Args.Empty, CartScreen>, router: Router) :
    NavigationScreen<CartScreen>(initialScreen, router)

