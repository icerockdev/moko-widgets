/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.utils.localized
import platform.UIKit.UITabBarController
import platform.UIKit.UITabBarItem
import platform.UIKit.tabBarItem

class BottomNavigationViewController(
    screen: BottomNavigationScreen,
    private val screenFactory: ScreenFactory
) : UITabBarController(nibName = null, bundle = null) {

    init {
        val items = screen.items
        val viewControllers = items.map {
            val childScreen = screenFactory.instantiateScreen(it.screen)
            childScreen.createViewController().apply {
                tabBarItem = UITabBarItem(title = it.title.localized(), image = null, selectedImage = null)
            }
        }
        setViewControllers(viewControllers = viewControllers)
    }
}
