/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.NavigationItem
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getParentScreen
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

class ProductsScreen : WidgetScreen<Args.Empty>(), NavigationItem {
    override val navigationTitle: StringDesc get() = "Products".desc()

    override fun createContentWidget(): Widget {
        return with(WidgetScope()) {
            container(
                styled = {
                    it.copy(
                        size = WidgetSize.Const(
                            SizeSpec.AsParent,
                            SizeSpec.AsParent
                        )
                    )
                },
                children = mapOf(
                    button(
                        text = const("go to product"),
                        onTap = ::onProductPressed
                    ) to Alignment.CENTER
                )
            )
        }
    }

    private fun onProductPressed() {
        println("go to product!")
        getParentScreen<Parent>().routeToProduct(10)
    }

    interface Parent {
        fun routeToProduct(productId: Int)
    }
}