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
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef
class ConstraintWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ConstraintWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    @Suppress("RemoveRedundantQualifierName")
    builder: ConstraintWidget.ChildrenBuilder.() -> ConstraintsApi.() -> Unit
) : Widget<WS>(), OptionalId<ConstraintWidget.Id> {

    val children: List<Widget<out WidgetSize>>
    val constraints: ConstraintsApi.() -> Unit

    init {
        ChildrenBuilder().run {
            constraints = this.builder()
            children = build()
        }
    }

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    class ChildrenBuilder internal constructor() {
        private val children: MutableList<Widget<out WidgetSize>> = mutableListOf()

        val root: ConstraintItem = ConstraintItem.Root

        operator fun Widget<out WidgetSize>.unaryPlus(): ConstraintItem.Child {
            children.add(this)
            return ConstraintItem.Child(this)
        }

        fun constraints(block: ConstraintsApi.() -> Unit): ConstraintsApi.() -> Unit {
            return block
        }

        fun build(): List<Widget<out WidgetSize>> {
            return children
        }
    }

    interface Id : Theme.Id
}

sealed class ConstraintItem {
    object Root : ConstraintItem()
    data class Child(val widget: Widget<out WidgetSize>) : ConstraintItem()
}

interface ConstraintsApi {
    infix fun ConstraintItem.Child.leftToRight(to: ConstraintItem)
    infix fun ConstraintItem.Child.leftToLeft(to: ConstraintItem)
    infix fun ConstraintItem.Child.rightToRight(to: ConstraintItem)
    infix fun ConstraintItem.Child.rightToLeft(to: ConstraintItem)
    infix fun ConstraintItem.Child.topToTop(to: ConstraintItem)
    infix fun ConstraintItem.Child.topToBottom(to: ConstraintItem)
    infix fun ConstraintItem.Child.centerYToCenterY(to: ConstraintItem)
    infix fun ConstraintItem.Child.centerXToCenterX(to: ConstraintItem)
    infix fun ConstraintItem.Child.bottomToBottom(to: ConstraintItem)
    infix fun ConstraintItem.Child.bottomToTop(to: ConstraintItem)

    infix fun ConstraintItem.Child.fillWidth(to: ConstraintItem) {
        this leftToLeft to
        this rightToRight to
    }
}
