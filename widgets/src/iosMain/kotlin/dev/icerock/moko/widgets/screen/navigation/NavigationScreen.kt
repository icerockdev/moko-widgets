/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.TypedScreenDesc
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.UIKit.navigationItem

actual abstract class NavigationScreen<S> actual constructor(
    private val initialScreen: TypedScreenDesc<Args.Empty, S>,
    router: Router
) : Screen<Args.Empty>() where S : Screen<Args.Empty>, S : NavigationItem {

    init {
        router.navigationScreen = this
    }

    private var navigationController: UINavigationController? = null

    override fun createViewController(): UIViewController {
        val controller = UINavigationController().also {
            navigationController = it
        }
        val rootScreen = initialScreen.instantiate()
        val rootViewController = rootScreen.viewController
        updateNavigation(rootScreen, rootViewController)
        controller.setViewControllers(listOf(rootViewController))

        return controller
    }

    private fun updateNavigation(
        navigationItem: NavigationItem,
        viewController: UIViewController
    ) {
        when (val navBar = navigationItem.navigationBar) {
            is NavigationBar.None -> navigationController?.navigationBarHidden = true
            is NavigationBar.Normal -> {
                navigationController?.navigationBarHidden = false
                viewController.navigationItem.title = navBar.title.localized()
            }
        }
    }

    actual class Router {
        var navigationScreen: NavigationScreen<*>? = null

        internal actual fun <T, Arg : Args, S> createPushRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (T) -> Arg
        ): Route<T> where S : Screen<Arg>, S : NavigationItem {
            return object : Route<T> {
                override fun route(source: Screen<*>, arg: T) {
                    val newScreen = destination.instantiate()
                    newScreen.arg = inputMapper(arg)
                    val screenViewController: UIViewController = newScreen.viewController
                    navigationScreen!!.run {
                        updateNavigation(newScreen, screenViewController)
                        navigationController?.pushViewController(screenViewController, animated = true)
                    }
                }
            }
        }

        internal actual fun <IT, Arg : Args, OT, R : Parcelable, S> createPushResultRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (IT) -> Arg,
            outputMapper: (R) -> OT
        ): RouteWithResult<IT, OT> where S : Screen<Arg>, S : Resultable<R>, S : NavigationItem {
            return object : RouteWithResult<IT, OT> {
                override fun route(source: Screen<*>, arg: IT, handler: RouteHandler<OT>) {
                    val newScreen = destination.instantiate()
                    newScreen.arg = inputMapper(arg)
                    val screenViewController: UIViewController = newScreen.viewController
                    navigationScreen!!.run {
                        updateNavigation(newScreen, screenViewController)
                        navigationController?.pushViewController(screenViewController, animated = true)
                    }
                    TODO("output")
                }

                override val resultMapper: (Parcelable) -> OT = { outputMapper(it as R) }
            }
        }

        internal actual fun <T, Arg : Args, S> createReplaceRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (T) -> Arg
        ): Route<T> where S : Screen<Arg>, S : NavigationItem {
            return object : Route<T> {
                override fun route(source: Screen<*>, arg: T) {
                    val newScreen = destination.instantiate()
                    newScreen.arg = inputMapper(arg)
                    val screenViewController: UIViewController = newScreen.viewController
                    navigationScreen!!.run {
                        updateNavigation(newScreen, screenViewController)
                        navigationController?.setViewControllers(listOf(screenViewController), animated = true)
                    }
                }
            }
        }
    }
}
