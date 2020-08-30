/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.ClickableViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(ClickableViewFactory::class)
class ClickableWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ClickableWidget<out WidgetSize>>,
    override val id: Id?,
    val child: Widget<WS>,
    val onClick: () -> Unit
) : Widget<WS>(), OptionalId<ClickableWidget.Id> {

    override val size: WS = child.size

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<ClickableWidget<out WidgetSize>>
    interface Category : Theme.Category<ClickableWidget<out WidgetSize>>

    object DefaultCategory : Category
}
