/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.UIKit.navigationItem
import kotlin.reflect.KClass

class NavigationViewController(val screen: NavigationScreen) :
    UINavigationController(nibName = null, bundle = null) {

    init {
        val rootScreen = screen.rootScreen.instantiate()
        val rootViewController = rootScreen.createViewController()
        setViewControllers(listOf(rootViewController))

        rootViewController.navigationItem.title = rootScreen.toString()
    }

    inner class Nav : Navigation {
        override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
            val newScreen = screen.instantiate()
            val screenViewController: UIViewController = newScreen.createViewController()
            screenViewController.navigationItem.title = newScreen.toString()
            pushViewController(screenViewController, animated = true)
        }

        override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
            val newScreen = screen.instantiate()
            newScreen.setArgument(argument)
            val screenViewController: UIViewController = newScreen.createViewController()
            screenViewController.navigationItem.title = newScreen.toString()
            pushViewController(screenViewController, animated = true)
        }
    }
}