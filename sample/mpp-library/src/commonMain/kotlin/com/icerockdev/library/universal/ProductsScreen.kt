/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.factory.DefaultContainerWidgetViewFactory
import dev.icerock.moko.widgets.factory.DefaultContainerWidgetViewFactoryBase
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.NavigationItem
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getParentScreen
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

class ProductsScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem {
    override val navigationTitle: StringDesc get() = "Products".desc()

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        return with(theme) {
            container(
                factory = DefaultContainerWidgetViewFactory(
                    DefaultContainerWidgetViewFactoryBase.Style(
                        background = Background(
                            fill = Fill.Solid(Color(0x66, 0x66, 0x66, 0xFF))
                        ),
                        padding = PaddingValues(4f)
                    )
                ),
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.AsParent),
                children = mapOf(
                    button(
                        size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.AsParent),
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