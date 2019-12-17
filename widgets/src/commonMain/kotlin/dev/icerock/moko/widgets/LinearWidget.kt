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
import dev.icerock.moko.widgets.factory.LinearViewFactory
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef(LinearViewFactory::class)
class LinearWidget<WS : WidgetSize>(
    private val factory: ViewFactory<LinearWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    val orientation: Orientation = Orientation.VERTICAL,
    @Suppress("RemoveRedundantQualifierName")
    builder: LinearWidget.ChildrenBuilder.() -> Unit
) : Widget<WS>(), OptionalId<LinearWidget.Id> {

    val children: List<Widget<out WidgetSize>> = ChildrenBuilder().run {
        builder()
        build()
    }

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    class ChildrenBuilder {
        private val children: MutableList<Widget<out WidgetSize>> = mutableListOf()

        // TODO set limit of size by orientation
        operator fun Widget<out WidgetSize>.unaryPlus() {
            children.add(this)
        }

        fun build(): List<Widget<out WidgetSize>> {
            return children
        }
    }

    interface Id : Theme.Id<LinearWidget<out WidgetSize>>
    interface Category : Theme.Category<LinearWidget<out WidgetSize>>

    object DefaultCategory : Category
}
