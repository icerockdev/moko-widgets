/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.Screen
import platform.UIKit.*

actual abstract class BottomNavigationScreen actual constructor(
    router: Router,
    builder: BottomNavigationItem.Builder.() -> Unit
) : Screen<Args.Empty>() {

    init {
        router.bottomNavigationScreen = this
    }

    actual val items: List<BottomNavigationItem> =
        BottomNavigationItem.Builder().apply(builder).build()

    private var tabBarController: UITabBarController? = null

    override fun createViewController(): UIViewController {
        val controller = UITabBarController()
        val items = items
        val viewControllers = items.map { item ->
            val childScreen = item.screenDesc.instantiate()
            childScreen.viewController.apply {
                tabBarItem = UITabBarItem(
                    title = if (titleMode != TitleVisibilityMode.UNLABELED) item.title.localized() else "",
                    image = item.icon?.toUIImage(),
                    tag = 0
                )
            }
        }
        controller.setViewControllers(viewControllers = viewControllers)
        controller.tabBar.barTintColor = bottomNavigationColor?.toUIColor()
        controller.tabBar.selectedImageTintColor = selectedItemColor?.toUIColor()
        controller.tabBar.unselectedItemTintColor = unselectedItemColor?.toUIColor()
        tabBarController = controller

        return controller
    }

    actual var selectedItemId: Int
        get() = tabBarController?.selectedIndex?.let {
            items[it.toInt()].id
        } ?: -1
        set(value) {
            tabBarController?.setSelectedIndex(items.indexOfFirst { it.id == value }.toULong())
        }
    actual var bottomNavigationColor: Color? = null
        set(value) {
            field = value
            tabBarController?.also {
                it.tabBar.barTintColor = value?.toUIColor()
            }
        }

    actual var selectedItemColor: Color? = null
        set(value) {
            field = value
            tabBarController?.also {
                it.tabBar.selectedImageTintColor = value?.toUIColor()
            }
        }

    actual var unselectedItemColor: Color? = null
        set(value) {
            field = value
            tabBarController?.also {
                it.tabBar.unselectedItemTintColor = value?.toUIColor()
            }
        }

    actual var titleMode: TitleVisibilityMode? = null

    actual class Router {
        var bottomNavigationScreen: BottomNavigationScreen? = null

        actual fun createChangeTabRoute(itemId: Int): Route<Unit> {
            return object : Route<Unit> {
                override fun route(source: Screen<*>, arg: Unit) {
                    bottomNavigationScreen!!.selectedItemId = itemId
                }
            }
        }
    }
}
