/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.NavigationBar
import dev.icerock.moko.widgets.screen.NavigationItem
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

// LIB SIDE

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

abstract class Screen<Arg : Args>() {
    fun <T : Any> createEventsDispatcher(): EventsDispatcher<T> {
        TODO()
    }

    inline fun <reified VM : ViewModel, Key : Any> getViewModel(key: Key, crossinline viewModelFactory: () -> VM): VM {
        TODO()
    }
}

class RouteHandlerProperty<T> : ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
    override fun getValue(thisRef: Screen<*>, property: KProperty<*>): RouteHandler<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun <T> Screen<*>.registerRouteHandler(handler: (T?) -> Unit): RouteHandlerProperty<T> {
    TODO()
}

abstract class WidgetScreen<Arg : Args>() : Screen<Arg>() {
    open val isDismissKeyboardOnTap: Boolean = false

    open val isKeyboardResizeContent: Boolean = false

    abstract fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
}

interface Resultable<R : Parcelable> {
    val screenResult: R?
}

interface Route<T> {
    fun route(source: Screen<*>, arg: T)
}

fun Route<Unit>.route(source: Screen<*>) {
    route(source = source, arg = Unit)
}

interface RouteHandler<T>

interface RouteWithResult<Arg, T> {
    fun route(
        source: Screen<*>,
        arg: Arg,
        handler: RouteHandler<T>
    )
}

fun <T> RouteWithResult<Unit, T>.route(source: Screen<*>, handler: RouteHandler<T>) {
    route(source = source, arg = Unit, handler = handler)
}

fun ScreenFactory<Args.Empty, NavigationScreen<*>>.createRouter(): NavigationScreen.Router {
    println(this)
    return NavigationScreen.Router()
}

class NavigationScreen<T>(
    initialScreen: TypedScreenDesc<Args.Empty, T>,
    router: Router
) : Screen<Args.Empty>() where T : Screen<Args.Empty>, T : NavigationItem {

    class Router() {
        private fun <T, Arg : Args, S> createRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (T) -> Arg
        ): Route<T> where S : Screen<Arg>, S : NavigationItem = TODO()

        fun <S> createRoute(
            destination: TypedScreenDesc<Args.Empty, S>
        ): Route<Unit> where S : Screen<Args.Empty>, S : NavigationItem = createRouteInternal(
            destination = destination,
            inputMapper = { Args.Empty }
        )

        fun <T, P : Parcelable, S> createRoute(
            destination: TypedScreenDesc<Args.Parcel<P>, S>,
            inputMapper: (T) -> P
        ): Route<T> where S : Screen<Args.Parcel<P>>, S : NavigationItem = createRouteInternal(
            destination = destination,
            inputMapper = { Args.Parcel(inputMapper(it)) }
        )

        private fun <IT, Arg : Args, OT, R : Parcelable, S> createResultRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (IT) -> Arg,
            outputMapper: (R) -> OT
        ): RouteWithResult<IT, OT> where S : Screen<Arg>, S : Resultable<R>, S : NavigationItem = TODO()

        fun <OT, R : Parcelable, S> createResultRoute(
            destination: TypedScreenDesc<Args.Empty, S>,
            outputMapper: (R) -> OT
        ): RouteWithResult<Unit, OT> where S : Screen<Args.Empty>, S : Resultable<R>, S : NavigationItem = TODO()

        fun <IT, P : Parcelable, OT, R : Parcelable, S> createResultRoute(
            destination: TypedScreenDesc<Args.Parcel<P>, S>,
            inputMapper: (IT) -> P,
            outputMapper: (R) -> OT
        ): RouteWithResult<IT, OT> where S : Screen<Args.Parcel<P>>, S : Resultable<R>, S : NavigationItem =
            createResultRouteInternal(
                destination = destination,
                inputMapper = { Args.Parcel(inputMapper(it)) },
                outputMapper = outputMapper
            )
    }
}

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
                    authRoute = router.createRoute(authScreen),
                    mainRoute = router.createRoute(
                        destination = mainScreen,
                        inputMapper = { MainScreen.Arg(token = it) }
                    ),
                    registerRoute = router.createResultRoute(
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
