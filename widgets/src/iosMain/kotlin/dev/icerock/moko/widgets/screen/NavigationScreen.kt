/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.UIKit.navigationItem
import kotlin.reflect.KClass

actual abstract class NavigationScreen actual constructor(
    private val screenFactory: ScreenFactory
) : Screen<Args.Empty>() {

    actual abstract val rootScreen: RootNavigationScreen

    private var navigationController: UINavigationController? = null

    override fun createViewController(): UIViewController {
        val controller = UINavigationController()
        val rootScreen = screenFactory.instantiateScreen(rootScreen.screenClass)
        rootScreen as NavigationItem // RootNavigationScreen require NavigationItem interface, so here we know that
        // instance is implementation of this interface
        rootScreen.parent = this
        val rootViewController = rootScreen.createViewController()
        updateNavigation(rootScreen, rootViewController)
        controller.setViewControllers(listOf(rootViewController))

        navigationController = controller

        return controller
    }

    actual fun <S> routeToScreen(
        screen: KClass<out S>
    ) where S : Screen<Args.Empty>, S : NavigationItem {
        val newScreen = screenFactory.instantiateScreen(screen)
        pushScreen(newScreen)
    }

    actual fun <A : Parcelable, S> routeToScreen(
        screen: KClass<out S>,
        args: A
    ) where S : Screen<Args.Parcel<A>>, S : NavigationItem {
        val newScreen = screenFactory.instantiateScreen(screen)
        newScreen.setArgument(args)
        pushScreen(newScreen)
    }

    private fun <A : Args, S> pushScreen(screen: S) where S : Screen<A>, S : NavigationItem {
        screen.parent = this
        val screenViewController: UIViewController = screen.createViewController()
        updateNavigation(screen, screenViewController)
        navigationController?.pushViewController(screenViewController, animated = true)
    }

    actual fun <S> setScreen(screen: KClass<out S>) where S : Screen<Args.Empty>, S : NavigationItem {
        val newScreen = screenFactory.instantiateScreen(screen)
        newScreen.parent = this
        val screenViewController: UIViewController = newScreen.createViewController()
        updateNavigation(newScreen, screenViewController)
        navigationController?.setViewControllers(listOf(screenViewController), animated = true)
    }

    actual fun <A : Parcelable, S> setScreen(
        screen: KClass<out S>,
        args: A
    ) where S : Screen<Args.Parcel<A>>, S : NavigationItem {
        val newScreen = screenFactory.instantiateScreen(screen)
        newScreen.parent = this
        newScreen.setArgument(args)
        val screenViewController: UIViewController = newScreen.createViewController()
        updateNavigation(newScreen, screenViewController)
        navigationController?.setViewControllers(listOf(screenViewController), animated = true)
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
}
