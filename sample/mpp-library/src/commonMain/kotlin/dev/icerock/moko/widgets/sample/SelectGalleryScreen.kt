/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.widget.ButtonWidget
import dev.icerock.moko.widgets.core.widget.LinearWidget
import dev.icerock.moko.widgets.core.widget.button
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.core.screen.navigation.Route
import dev.icerock.moko.widgets.core.screen.navigation.route
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@Suppress("MagicNumber")
class SelectGalleryScreen(
    theme: Theme,
    private val routes: List<RouteInfo>
) : ScrollContentScreen<Args.Empty>(theme), NavigationItem {
    override val androidStatusBarColor: Color? = Color(0x0000FFFF)
    override val isLightStatusBar: Boolean? = false

    override val navigationBar: NavigationBar = NavigationBar.Normal(
        title = "Select gallery".desc(),
        styles = NavigationBar.Styles(
            backgroundColor = Color(0x4444EEFF)
        )
    )

    override fun LinearWidget.ChildrenBuilder.fillLinear(theme: Theme) {
        with(theme) {
            routes.forEach { routeInfo ->
                +button(
                    size = WidgetSize.WidthAsParentHeightWrapContent,
                    content = ButtonWidget.Content.Text(Value.data(routeInfo.name)),
                    onTap = { routeInfo.route.route() }
                )
            }
        }
    }

    data class RouteInfo(
        val name: StringDesc,
        val route: Route<Unit>
    )
}
