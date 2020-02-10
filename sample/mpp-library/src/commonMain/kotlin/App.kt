/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.icerockdev.library.AppTheme
import com.icerockdev.library.MR
import com.icerockdev.library.SharedFactory
import com.icerockdev.library.universal.CartScreen
import com.icerockdev.library.universal.InfoWebViewScreen
import com.icerockdev.library.universal.LoginScreen
import com.icerockdev.library.universal.LoginViewModel
import com.icerockdev.library.universal.PlatformProfileScreen
import com.icerockdev.library.universal.ProductScreen
import com.icerockdev.library.universal.ProductsScreen
import com.icerockdev.library.universal.ProfileScreen
import com.icerockdev.library.universal.WidgetsScreen
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.factory.ButtonWithIconViewFactory
import dev.icerock.moko.widgets.factory.IconGravity
import dev.icerock.moko.widgets.factory.SystemImageViewFactory
import dev.icerock.moko.widgets.factory.SystemInputViewFactory
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
import dev.icerock.moko.widgets.screen.navigation.SelectStates
import dev.icerock.moko.widgets.screen.navigation.createPushResultRoute
import dev.icerock.moko.widgets.screen.navigation.createPushRoute
import dev.icerock.moko.widgets.screen.navigation.createReplaceRoute
import dev.icerock.moko.widgets.screen.navigation.createRouter
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.CornerRadiusValue
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

class App() : BaseApplication() {

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
            factory[LoginScreen.Id.RegistrationButtonId] = ButtonWithIconViewFactory(
                icon = MR.images.stars_black_18,
                iconGravity = IconGravity.TEXT_END,
                iconPadding = 8.0f,
                padding = PaddingValues(padding = 16f),
                background = StateBackground(
                    normal = Background(fill = Fill.Solid(color = Color(0xAAFFFFFF))),
                    pressed = Background(fill = Fill.Solid(color = Color(0x88FFFFFF))),
                    disabled = Background(fill = Fill.Solid(color = Color(0x55FFFFFF)))
                )
            )
            factory[ImageWidget.DefaultCategory] = SystemImageViewFactory(
                cornerRadiusValue = CornerRadiusValue(16.0f)
            )
        }

        val widgetsTheme = Theme(theme) {
            factory[InputWidget.DefaultCategory] = SystemInputViewFactory(
                textStyle = TextStyle(
                    size = 16,
                    color = Color(0x16171AFF)
                )
            )
        }

        val mainScreen = registerScreen(MainBottomNavigationScreen::class) {
            val bottomRouter = createRouter()

            val cartNavigation = registerScreen(NavigationScreen::class) {
                val navigationRouter = createRouter()
                val profileScreen = registerScreen(ProfileScreen::class) {
                    PlatformProfileScreen(navigationRouter.createPopRoute())
                }
                val cartScreen = registerScreen(CartScreen::class) {
                    CartScreen(
                        theme = theme,
                        profileRoute = navigationRouter.createPushRoute(profileScreen) { ProfileScreen.Arg(it) }
                    )
                }
                CartNavigationScreen(
                    initialScreen = cartScreen,
                    router = navigationRouter
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
                WidgetsScreen(sharedFactory, widgetsTheme, AppTheme.PostsCollection)
            }

            MainBottomNavigationScreen(bottomRouter) {
                tab(
                    id = 1,
                    title = "Products".desc(),
                    selectedIcon = MR.images.cart_black_18,
                    unselectedIcon = MR.images.home_black_18,
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

        return registerScreen(RootNavigationScreen::class) {
            val router = createRouter()

            val regScreen = registerScreen(RegisterScreen::class) {
                RegisterScreen(theme)
            }

            val oauthScreen = registerScreen(InfoWebViewScreen::class) {
                InfoWebViewScreen(
                    theme = loginTheme
                )
            }

            val loginScreen = registerScreen(LoginScreen::class) {
                LoginScreen(
                    theme = loginTheme,
                    mainRoute = router.createReplaceRoute(mainScreen),
                    registerRoute = router.createPushResultRoute(regScreen) { it.token },
                    infoWebViewRoute = router.createPushRoute(oauthScreen) {
                        InfoWebViewScreen.WebViewArgs(it)
                    }
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

    init {
        bottomNavigationColor = Color(0x6518f4FF)

        itemStateColors = SelectStates(
            selected = Color(0xfdfffdFF),
            unselected = Color(0xc0a3f9FF)
        )
    }
}

class RootNavigationScreen(initialScreen: TypedScreenDesc<Args.Empty, LoginScreen>, router: Router) :
    NavigationScreen<LoginScreen>(initialScreen, router)

class ProductsNavigationScreen(initialScreen: TypedScreenDesc<Args.Empty, ProductsScreen>, router: Router) :
    NavigationScreen<ProductsScreen>(initialScreen, router)

class CartNavigationScreen(initialScreen: TypedScreenDesc<Args.Empty, CartScreen>, router: Router) :
    NavigationScreen<CartScreen>(initialScreen, router)

