/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.UIKit.navigationItem
import kotlin.reflect.KClass

class NavigationViewController(
    screen: NavigationScreen,
    private val screenFactory: ScreenFactory
) : UINavigationController(nibName = null, bundle = null) {

    init {
        val rootScreen = screenFactory.instantiateScreen(screen.rootScreen)
        val rootViewController = rootScreen.createViewController()
        setViewControllers(listOf(rootViewController))
    }

    inner class Nav : Navigation {
        override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
            val newScreen = screenFactory.instantiateScreen(screen)
            val screenViewController: UIViewController = newScreen.createViewController()
            screenViewController.navigationItem.title = newScreen.toString()
            pushViewController(screenViewController, animated = true)
        }

        override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
            val newScreen = screenFactory.instantiateScreen(screen)
            newScreen.setArgument(argument)
            val screenViewController: UIViewController = newScreen.createViewController()
            screenViewController.navigationItem.title = newScreen.toString()
            pushViewController(screenViewController, animated = true)
        }
    }
}