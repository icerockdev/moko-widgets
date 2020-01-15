/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text

class CartScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem {
    override val navigationBar = NavigationBar.Normal(title = "Cart".desc())

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            container(size = WidgetSize.AsParent) {
                center {
                    text(
                        size = WidgetSize.WrapContent,
                        text = const("cart")
                    )
                }
            }
        }
    }
}
