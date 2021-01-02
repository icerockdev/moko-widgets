/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.VerticalPageViewFactory
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(VerticalPageViewFactory::class)
class VerticalPageWidget<WS : WidgetSize>(
    private val factory: ViewFactory<VerticalPageWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val header: Widget<out WidgetSize>? = null,
    val body: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.MatchConstraint>>,
    val footer: Widget<out WidgetSize>? = null
) : Widget<WS>(), OptionalId<VerticalPageWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<VerticalPageWidget<out WidgetSize>>
    interface Category : Theme.Category<VerticalPageWidget<out WidgetSize>>

    object DefaultCategory : Category
}
