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
import dev.icerock.moko.widgets.factory.ContainerViewFactory
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef(ContainerViewFactory::class)
class ContainerWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ContainerWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    @Suppress("RemoveRedundantQualifierName")
    builder: ContainerWidget.ChildrenBuilder.() -> Unit
) : Widget<WS>(), OptionalId<ContainerWidget.Id> {

    val children: Map<out Widget<out WidgetSize>, Alignment> = ChildrenBuilder().run {
        builder()
        build()
    }

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    class ChildrenBuilder {
        private val children: MutableMap<Widget<out WidgetSize>, Alignment> = mutableMapOf()

        // TODO change from lambda to argument (required only one item)
        fun center(block: () -> Widget<out WidgetSize>) {
            children[block.invoke()] = Alignment.CENTER
        }

        fun left(block: () -> Widget<out WidgetSize>) {
            children[block.invoke()] = Alignment.LEFT
        }

        fun right(block: () -> Widget<out WidgetSize>) {
            children[block.invoke()] = Alignment.RIGHT
        }

        fun top(block: () -> Widget<out WidgetSize>) {
            children[block.invoke()] = Alignment.TOP
        }

        fun bottom(block: () -> Widget<out WidgetSize>) {
            children[block.invoke()] = Alignment.BOTTOM
        }

        fun build(): Map<out Widget<out WidgetSize>, Alignment> {
            return children
        }
    }

    interface Id : Theme.Id<ContainerWidget<out WidgetSize>>
    interface Category : Theme.Category<ContainerWidget<out WidgetSize>>

    object DefaultCategory : Category
}
