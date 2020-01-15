/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.screen.navigation.Route
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

class ProductsScreen(
    private val theme: Theme,
    private val productRoute: Route<Int>
) : WidgetScreen<Args.Empty>(), NavigationItem {
    override val navigationBar = NavigationBar.Normal(title = "Products".desc())

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            container(size = WidgetSize.AsParent) {
                center {
                    button(
                        size = WidgetSize.WrapContent,
                        content = ButtonWidget.Content.Text(Value.data("Go to product".desc())),
                        onTap = ::onProductPressed
                    )
                }
            }
        }
    }

    private fun onProductPressed() {
        println("go to product!")
        productRoute.route(this, 10)
    }
}
