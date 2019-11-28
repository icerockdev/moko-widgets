/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef
class ContainerWidget<WS : WidgetSize>(
    val factory: ViewFactory<ContainerWidget<out WidgetSize>>,
    val size: WS,
    override val id: Id?,
    val children: Map<out Widget<out WidgetSize>, Alignment>
) : Widget<WS>(), OptionalId<ContainerWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id
}
