/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.ScreenDesc
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.screen.navigation.NavigationScreen
import dev.icerock.moko.widgets.screen.navigation.Resultable
import dev.icerock.moko.widgets.screen.navigation.Route
import dev.icerock.moko.widgets.screen.navigation.RouteWithResult
import dev.icerock.moko.widgets.screen.navigation.createPushResultRoute
import dev.icerock.moko.widgets.screen.navigation.createPushRoute
import dev.icerock.moko.widgets.screen.navigation.createRouter
import dev.icerock.moko.widgets.screen.navigation.registerRouteHandler
import dev.icerock.moko.widgets.screen.navigation.route

// APP SIDE

class SplashScreen(
    private val mainRoute: Route<String>,
    private val authRoute: Route<Unit>,
    private val registerRoute: RouteWithResult<String, String>
) : WidgetScreen<Args.Empty>(),
    SplashViewModel.EventsListener, NavigationItem {

    private val registerHandler by registerRouteHandler<String> {
        if (it == null) {
            println("no results from screen (moved back without result set)")
        } else {
            println("registration result $it")
        }
    }

    override val navigationBar: NavigationBar = NavigationBar.None

    override fun createContentWidget() = TODO()

    override fun routeToMain() {
        mainRoute.route(
            source = this,
            arg = "token"
        )
    }

    override fun routeToAuth() {
        authRoute.route(source = this)
    }

    override fun routeToRegister() {
        registerRoute.route(
            source = this,
            arg = "reg",
            handler = registerHandler
        )
    }
}

class SplashViewModel : ViewModel() {

    interface EventsListener {
        fun routeToMain()
        fun routeToAuth()
        fun routeToRegister()
    }
}

class MainScreen(
    private val theme: Theme
) : WidgetScreen<Args.Parcel<MainScreen.Arg>>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.None

    override fun createContentWidget() = TODO()

    @Parcelize
    data class Arg(
        val token: String
    ) : Parcelable
}

class AuthScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.None

    override fun createContentWidget() = TODO()
}

open class RegisterScreen(
    private val theme: Theme
) : WidgetScreen<Args.Parcel<RegisterScreen.Input>>(),
    NavigationItem, Resultable<RegisterScreen.Output> {

    override var screenResult: Output? = null

    override val navigationBar: NavigationBar = NavigationBar.None

    override fun createContentWidget() = TODO()

    private fun something() {
        screenResult = Output("test")
    }

    @Parcelize
    data class Input(val input: String) : Parcelable

    @Parcelize
    data class Output(val output: String) : Parcelable
}

class ARegist(theme: Theme) : RegisterScreen(theme)

class NewApp : BaseApplication() {
    override fun setup(): ScreenDesc<Args.Empty> {
        val theme = Theme {}

        val authScreen = registerScreen(AuthScreen::class) {
            AuthScreen(theme)
        }
        val registerScreen = registerScreen(RegisterScreen::class) {
            ARegist(theme)
        }
        val mainScreen = registerScreen(MainScreen::class) {
            MainScreen(theme)
        }

        val navigationScreen = registerScreen(NavigationScreen::class) {
            val router = createRouter()

            val splashScreen = registerScreen(SplashScreen::class) {
                SplashScreen(
                    authRoute = router.createPushRoute(authScreen),
                    mainRoute = router.createPushRoute(
                        destination = mainScreen,
                        inputMapper = { MainScreen.Arg(token = it) }
                    ),
                    registerRoute = router.createPushResultRoute(
                        destination = registerScreen,
                        inputMapper = { RegisterScreen.Input(it) },
                        outputMapper = { it.output }
                    )
                )
            }

            NavigationScreen(
                initialScreen = splashScreen,
                router = router
            )
        }

        return navigationScreen
    }
}
