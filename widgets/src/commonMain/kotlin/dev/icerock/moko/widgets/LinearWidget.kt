/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var linearWidgetViewFactory: VFC<LinearWidget>

class LinearWidget(
    private val factory: VFC<LinearWidget>,
    val style: Style,
    val childs: List<Widget>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    data class Style(
        val background: Background = Background(),
        val orientation: Orientation = Orientation.VERTICAL,
        val size: WidgetSize = WidgetSize(
            width = SizeSpec.AS_PARENT,
            height = SizeSpec.AS_PARENT
        )
    )

    internal object FactoryKey : WidgetScope.Key
    internal object StyleKey : WidgetScope.Key
}

val WidgetScope.linearFactory: VFC<LinearWidget>
        by WidgetScope.readProperty(LinearWidget.FactoryKey, ::linearWidgetViewFactory)

var WidgetScope.Builder.linearFactory: VFC<LinearWidget>
        by WidgetScope.readWriteProperty(LinearWidget.FactoryKey, WidgetScope::linearFactory)

val WidgetScope.linearStyle: LinearWidget.Style
        by WidgetScope.readProperty(LinearWidget.StyleKey) { LinearWidget.Style() }

var WidgetScope.Builder.linearStyle: LinearWidget.Style
        by WidgetScope.readWriteProperty(LinearWidget.StyleKey, WidgetScope::linearStyle)

fun WidgetScope.linear(
    factory: VFC<LinearWidget> = this.linearFactory,
    style: LinearWidget.Style = this.linearStyle,
    childs: List<Widget>
) = LinearWidget(
    factory = factory,
    style = style,
    childs = childs
)
