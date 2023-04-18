/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen.navigation

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.BaseApplication
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.widgets.core.style.state.SelectableState
import dev.icerock.moko.widgets.core.utils.getStatusBarStyle
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UITabBarController
import platform.UIKit.UITabBarItem
import platform.UIKit.UIViewController
import platform.UIKit.tabBarItem

actual abstract class BottomNavigationScreen actual constructor(
    router: Router,
    builder: BottomNavigationItem.Builder.() -> Unit
) : Screen<Args.Empty>() {

    init {
        router.bottomNavigationScreen = this
    }

    private var tabBarController: UITabBarController? = null

    actual var itemStateColors: SelectableState<Color>? = null
        set(value) {
            field = value

            tabBarController?.tabBar?.unselectedItemTintColor = value?.unselected?.toUIColor()
            tabBarController?.tabBar?.selectedImageTintColor = value?.selected?.toUIColor()
        }

    actual val items: List<BottomNavigationItem> =
        BottomNavigationItem.Builder().apply(builder).build()

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

    actual var isTitleVisible: Boolean = true
        set(value) {
            field = value
            tabBarController?.viewControllers?.forEachIndexed { index, controller ->
                (controller as? UIViewController)?.tabBarItem?.title =
                    if (isTitleVisible) items[index].title.localized() else null
            }
        }

    override fun createViewController(isLightStatusBar: Boolean?): UIViewController {
        val controller = TabBarController(isLightStatusBar)
        val items = items
        val viewControllers = items.map { item ->
            val childScreen = item.screenDesc.instantiate()
            childScreen.viewController.apply {
                tabBarItem = UITabBarItem(
                    title = if (isTitleVisible) item.title.localized() else null,
                    image = item.stateIcons?.unselected?.toUIImage(),
                    selectedImage = item.stateIcons?.selected?.toUIImage()
                )
            }
        }
        controller.setViewControllers(viewControllers = viewControllers)
        controller.tabBar.barTintColor = bottomNavigationColor?.toUIColor()
        controller.tabBar.selectedImageTintColor = itemStateColors?.selected?.toUIColor()
        controller.tabBar.unselectedItemTintColor = itemStateColors?.unselected?.toUIColor()
        tabBarController = controller

        return controller
    }

    actual class Router {
        var bottomNavigationScreen: BottomNavigationScreen? = null

        actual fun createChangeTabRoute(itemId: Int): Route<Unit> {
            return object : Route<Unit> {
                override fun route(arg: Unit) {
                    bottomNavigationScreen!!.selectedItemId = itemId
                }
            }
        }
    }
}

private class TabBarController(
    private val isLightStatusBar: Boolean?
) : UITabBarController(nibName = null, bundle = null) {

    override fun preferredStatusBarStyle(): UIStatusBarStyle {
        val light = isLightStatusBar ?: BaseApplication.sharedInstance.isLightStatusBar
        return getStatusBarStyle(light) ?: super.preferredStatusBarStyle()
    }
}
