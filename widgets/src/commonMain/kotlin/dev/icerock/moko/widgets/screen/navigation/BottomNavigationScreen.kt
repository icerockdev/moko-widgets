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
import dev.icerock.moko.widgets.style.state.SelectableState

expect abstract class BottomNavigationScreen(
    router: Router,
    builder: BottomNavigationItem.Builder.() -> Unit
) : Screen<Args.Empty> {
    val items: List<BottomNavigationItem>

    var selectedItemId: Int
    var bottomNavigationColor: Color?
    var isTitleVisible: Boolean
    var itemStateColors: SelectableState<Color>?

    class Router {
        fun createChangeTabRoute(itemId: Int): Route<Unit>
    }
}

data class BottomNavigationItem(
    val id: Int,
    val title: StringDesc,
    val stateIcons: SelectableState<ImageResource>? = null,
    val screenDesc: ScreenDesc<Args.Empty>
) {
    class Builder() {
        private val tabs = mutableListOf<BottomNavigationItem>()

        private fun tab(
            id: Int,
            title: StringDesc,
            stateIcons: SelectableState<ImageResource>? = null,
            screenDesc: ScreenDesc<Args.Empty>
        ) {
            tabs.add(
                BottomNavigationItem(
                    id = id,
                    title = title,
                    stateIcons = stateIcons,
                    screenDesc = screenDesc
                )
            )
        }

        fun tab(
            id: Int,
            title: StringDesc,
            selectedIcon: ImageResource,
            unselectedIcon: ImageResource,
            screenDesc: ScreenDesc<Args.Empty>
        ) {
            tab(
                id = id,
                title = title,
                stateIcons = SelectableState(
                    selected = selectedIcon,
                    unselected = unselectedIcon
                ),
                screenDesc = screenDesc
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
                stateIcons = icon?.let { SelectableState(all = it) },
                screenDesc = screenDesc
            )
        }

        fun build(): List<BottomNavigationItem> = tabs
    }
}
