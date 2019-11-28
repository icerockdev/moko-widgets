/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.RequireId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef
class ScrollWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ScrollWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id,
    val child: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>> // TODO allow other sizes?
) : Widget<WS>(), RequireId<ScrollWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id
}