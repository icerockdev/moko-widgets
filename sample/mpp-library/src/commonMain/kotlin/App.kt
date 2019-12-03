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
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.buttonFactory
import dev.icerock.moko.widgets.constraintFactory
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.factory.DefaultButtonWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultButtonWidgetViewFactoryBase
import dev.icerock.moko.widgets.factory.DefaultConstraintWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultConstraintWidgetViewFactoryBase
import dev.icerock.moko.widgets.factory.DefaultImageWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultImageWidgetViewFactoryBase
import dev.icerock.moko.widgets.factory.DefaultInputWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultInputWidgetViewFactoryBase
import dev.icerock.moko.widgets.factory.DefaultListWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultListWidgetViewFactoryBase
import dev.icerock.moko.widgets.factory.DefaultTextWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultTextWidgetViewFactoryBase
import dev.icerock.moko.widgets.imageFactory
import dev.icerock.moko.widgets.inputFactory
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.setListFactory
import dev.icerock.moko.widgets.setTextFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Direction
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Shape
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.Colors
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextAlignment
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.utils.platformSpecific
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

        val loginTheme = Theme(theme) {
            constraintFactory = DefaultConstraintWidgetViewFactory(
                DefaultConstraintWidgetViewFactoryBase.Style(
                    padding = PaddingValues(16f),
                    background = Background(
                        fill = Fill.Gradient(
                            colors = listOf(Color(0x684ECEFF), Color(0x9077E6FF)),
                            direction = Direction.BOTTOM_TOP
                        )
                    )
                )
            )

            imageFactory = DefaultImageWidgetViewFactory(
                DefaultImageWidgetViewFactoryBase.Style(
                    scaleType = DefaultImageWidgetViewFactoryBase.ScaleType.FIT
                )
            )

            val corners = platformSpecific(android = 16f, ios = 25f)

            inputFactory = DefaultInputWidgetViewFactory(
                DefaultInputWidgetViewFactoryBase.Style(
                    margins = MarginValues(bottom = 8f),
                    padding = PaddingValues(start = 20f, end = 20f),
                    background = Background(
                        fill = Fill.Solid(Colors.white.copy(alpha = 0xAA)),
                        shape = Shape.Rectangle(cornerRadius = corners)
                    ),
                    underLineColor = Color(0x00000000),
                    labelTextStyle = TextStyle(
                        color = Colors.black
                    )
                )
            )

            buttonFactory = DefaultButtonWidgetViewFactory(
                DefaultButtonWidgetViewFactoryBase.Style(
                    margins = MarginValues(top = 8f),
                    background = StateBackground(
                        normal = Background(
                            fill = Fill.Solid(Colors.white),
                            shape = Shape.Rectangle(
                                cornerRadius = corners
                            )
                        ),
                        pressed = Background(),
                        disabled = Background()
                    ),
                    textStyle = TextStyle(
                        color = Colors.black
                    )
                )
            )
        }

        registerScreenFactory(RootBottomNavigationScreen::class) { RootBottomNavigationScreen(this) }
        registerScreenFactory(ProductsNavigationScreen::class) { ProductsNavigationScreen(this) }
        registerScreenFactory(CartNavigationScreen::class) { CartNavigationScreen(this) }
        registerScreenFactory(WidgetsScreen::class) { WidgetsScreen(sharedFactory, theme) }
        registerScreenFactory(ProductsScreen::class) { ProductsScreen(theme) }
        registerScreenFactory(CartScreen::class) { CartScreen(theme) }
        registerScreenFactory(ProductScreen::class) { ProductScreen(theme) }

        registerScreenFactory(LoginScreen::class) { LoginScreen(loginTheme) }
    }

    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return LoginScreen::class
    }
}
