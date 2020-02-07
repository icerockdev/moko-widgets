/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.ScreenDesc

expect abstract class BottomNavigationScreen(
    router: Router,
    builder: BottomNavigationItem.Builder.() -> Unit
) : Screen<Args.Empty> {
    val items: List<BottomNavigationItem>

    var selectedItemId: Int
    var bottomNavigationColor: Color?
    var unselectedItemColor: Color?
    var selectedItemColor: Color?
    var isTitleVisible: Boolean

    class Router {
        fun createChangeTabRoute(itemId: Int): Route<Unit>
    }
}

data class BottomNavigationItem(
    val id: Int,
    val title: StringDesc,
    val selectedIcon: ImageResource? = null,
    val unselectedIcon: ImageResource? = null,
    val screenDesc: ScreenDesc<Args.Empty>
) {
    class Builder() {
        private val tabs = mutableListOf<BottomNavigationItem>()

        fun tab(
            id: Int,
            title: StringDesc,
            selectedIcon: ImageResource? = null,
            unselectedIcon: ImageResource? = null,
            screenDesc: ScreenDesc<Args.Empty>
        ) {
            tabs.add(
                BottomNavigationItem(
                    id = id,
                    title = title,
                    selectedIcon = selectedIcon,
                    unselectedIcon = unselectedIcon,
                    screenDesc = screenDesc
                )
            )
        }

        fun tab(
            id: Int,
            title: StringDesc,
            icon: ImageResource? = null,
            screenDesc: ScreenDesc<Args.Empty>
        ) {
            tab(
                id = id,
                title = title,
                selectedIcon = icon,
                unselectedIcon = icon,
                screenDesc = screenDesc
            )
        }

        fun build(): List<BottomNavigationItem> = tabs
    }
}
