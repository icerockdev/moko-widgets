/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable
import platform.UIKit.UITabBarController
import platform.UIKit.UITabBarItem
import platform.UIKit.tabBarItem
import kotlin.reflect.KClass

class BottomNavigationViewController(val screen: BottomNavigationScreen) :
    UITabBarController(nibName = null, bundle = null) {

    init {
        val items = screen.items
        val viewControllers = items.map {
            val screen = it.screen.instantiate()
            screen.createViewController().apply {
                tabBarItem = UITabBarItem(title = it.title.toString(), image = null, selectedImage = null)
            }
        }
        setViewControllers(viewControllers = viewControllers)
    }

    inner class Nav : Navigation {
        override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
            val position = this@BottomNavigationViewController.screen.items.indexOfFirst {
                it.screen == screen
            }
            if (position == -1) return

            selectedIndex = position.toULong()
        }

        override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
            TODO()
//        val newScreen = screen.instantiate()
//        newScreen.setArgument(argument)
//        val screenViewController: UIViewController = newScreen.createViewController()
//        pushViewController(screenViewController, animated = true)
        }
    }
}
