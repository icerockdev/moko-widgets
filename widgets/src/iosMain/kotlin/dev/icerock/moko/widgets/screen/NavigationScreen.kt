/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.UIKit.navigationItem
import kotlin.reflect.KClass

actual abstract class NavigationScreen<S> actual constructor(
    private val screenFactory: ScreenFactory
) : Screen<Args.Empty>() where S : Screen<Args.Empty>, S : NavigationItem {

    actual abstract val rootScreen: KClass<out S>

    private var navigationController: UINavigationController? = null

    override fun createViewController(): UIViewController {
        val controller = UINavigationController()
        val rootScreen = screenFactory.instantiateScreen(rootScreen)
        rootScreen.parent = this
        val rootViewController = rootScreen.createViewController()
        rootViewController.navigationItem.title = rootScreen.navigationTitle.localized()
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
        screenViewController.navigationItem.title = screen.navigationTitle.localized()
        navigationController?.pushViewController(screenViewController, animated = true)
    }
}
