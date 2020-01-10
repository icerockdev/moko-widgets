/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.NavigationBar
import dev.icerock.moko.widgets.screen.NavigationItem
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.WidgetScreen
import kotlin.reflect.KClass

class ScreenFactory<Arg : Args, T : Screen<Arg>>

abstract class BaseApplication {
    abstract fun setup(): ScreenDesc<Args.Empty>

    val rootScreen: ScreenDesc<Args.Empty> = setup()

    fun <Arg : Args, T : Screen<Arg>> registerScreen(
        kClass: KClass<T>,
        factory: ScreenFactory<Arg, T>.() -> T
    ): TypedScreenDesc<Arg, T> = TypedScreenDesc(kClass, factory)
}

abstract class ScreenDesc<Arg : Args>(
    private val kClass: KClass<out Screen<Arg>>
) {
    abstract fun instantiate(): Screen<Arg>
}

class TypedScreenDesc<Arg : Args, T : Screen<Arg>>(
    kClass: KClass<T>,
    private val factory: ScreenFactory<Arg, T>.() -> T
) : ScreenDesc<Arg>(kClass) {

    override fun instantiate(): T = factory.invoke(ScreenFactory())
}

class SplashScreen(
    private val theme: Theme,
    private val createViewModel: (
        EventsDispatcher<SplashViewModel.EventsListener>
    ) -> SplashViewModel,
    private val mainRoute: Route<Args.Parcel<MainInput>>,
    private val authRoute: Route<Args.Empty>,
    private val registerRoute: ResultRoute<Args.Empty, RegisterResult>
) : WidgetScreen<Args.Empty>(),
    SplashViewModel.EventsListener, NavigationItem, ResultRoute.ResultHandler {

    override val navigationBar: NavigationBar = NavigationBar.None

    override fun createContentWidget() = TODO()

    override fun routeToMain() {
        mainRoute.route(this, MainInput(""))
    }

    override fun routeToAuth() {
        authRoute.route(this)
    }

    override fun routeToRegister() {
        registerRoute.route(this)
    }

    override fun onRouteResult(route: ResultRoute<*, *>, result: Any) {
        when (route) {
            registerRoute -> {
                result as RegisterResult
                println("token is ${result.token}")
            }
            else -> {
                println("fail!")
            }
        }
    }

    @Parcelize
    data class MainInput(
        val token: String
    ) : Parcelable

    @Parcelize
    data class RegisterResult(
        val token: String
    ) : Parcelable
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
) : WidgetScreen<Args.Empty>(), NavigationItem {

    override val navigationBar: NavigationBar = NavigationBar.None

    override fun createContentWidget() = TODO()
}

class ARegist(theme: Theme) : RegisterScreen(theme)

class NavigationScreen<T>(
    initialScreen: TypedScreenDesc<Args.Empty, T>,
    navigator: Navigator
) : Screen<Args.Empty>() where T : Screen<Args.Empty>, T : NavigationItem {

}

class Route<Arg : Args>

fun Route<Args.Empty>.route(source: Screen<*>) {
    println(this)
    TODO()
}

fun <IT : Parcelable> Route<Args.Parcel<IT>>.route(source: Screen<*>, data: IT) {
    println(this)
    TODO()
}

class ResultRoute<Arg : Args, T : Parcelable> {

    interface ResultHandler {
        fun onRouteResult(route: ResultRoute<*, *>, result: Any)
    }
}

fun <T : Parcelable, S> ResultRoute<Args.Empty, T>.route(source: S)
        where S : Screen<*>, S : ResultRoute.ResultHandler {
    println(this)
    TODO()
}

fun <IT : Parcelable, OT : Parcelable, S> ResultRoute<Args.Parcel<IT>, OT>.route(source: S, data: IT)
        where S : Screen<*>, S : ResultRoute.ResultHandler {
    println(this)
    TODO()
}

class Navigator {
    fun <Arg : Args, S> createRoute(
        destination: TypedScreenDesc<Arg, S>
    ): Route<Arg> where S : Screen<Arg>, S : NavigationItem = TODO()

    fun <T1 : Parcelable, T2 : Parcelable, S> createRoute(
        destination: TypedScreenDesc<Args.Parcel<T2>, S>,
        mapping: (T1) -> T2
    ): Route<Args.Parcel<T1>> where S : Screen<Args.Parcel<T2>>, S : NavigationItem = TODO()

    fun <Arg : Args, R : Parcelable, S> createResultRoute(
        destination: TypedScreenDesc<Arg, S>
    ): ResultRoute<Arg, R> where S : Screen<Arg>, S : NavigationItem = TODO()
}

fun ScreenFactory<Args.Empty, NavigationScreen<*>>.createNavigator(): Navigator {
    println(this)
    return Navigator()
}

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
            val navigator = createNavigator()

            val splashScreen = registerScreen(SplashScreen::class) {
                SplashScreen(
                    theme = theme,
                    createViewModel = { TODO() },
                    authRoute = navigator.createRoute(authScreen),
                    mainRoute = navigator.createRoute(mainScreen) { MainScreen.Arg(token = it.token) },
                    registerRoute = navigator.createResultRoute(registerScreen)
                )
            }

            NavigationScreen(
                initialScreen = splashScreen,
                navigator = navigator
            )
        }

        return navigationScreen
    }
}
