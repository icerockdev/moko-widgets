/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import androidx.constraintlayout.widget.ConstraintLayout
import dev.icerock.moko.widgets.ConstraintItem
import dev.icerock.moko.widgets.ConstraintWidget
import dev.icerock.moko.widgets.ConstraintsApi
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class DefaultConstraintWidgetViewFactory actual constructor(
    style: Style
) : DefaultConstraintWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ConstraintWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context

        val constraintLayout = ConstraintLayout(context)
        constraintLayout.applyStyle(style)

        val widgetViewBundle: Map<out Widget<out WidgetSize>, ViewBundle<out WidgetSize>> =
            widget.children.associateWith { childWidget ->
                val viewBundle = childWidget.buildView(
                    viewFactoryContext.copy(parent = constraintLayout)
                )

                constraintLayout.addView(viewBundle)

                // check id and autoassign if needed
                // TODO something can be wrong with autoassign?
                viewBundle.view.run {
                    if (id == View.NO_ID) {
                        id = childWidget.hashCode()
                    }
                }

                viewBundle
            }

        val constraintsHandler = ConstraintLayoutConstraintsApi(
            constraintLayout = constraintLayout,
            widgetViewBundle = widgetViewBundle
        )

        widget.constraints.invoke(constraintsHandler)

        return ViewBundle(
            view = constraintLayout,
            size = size,
            margins = style.margins
        )
    }

    private fun ConstraintLayout.addView(viewBundle: ViewBundle<out WidgetSize>) {
        val widgetSize = viewBundle.size
        val dm = viewBundle.view.resources.displayMetrics
        val view = viewBundle.view
        val lp = when (widgetSize) {
            is WidgetSize.Const<out SizeSpec, out SizeSpec> -> {
                ConstraintLayout.LayoutParams(
                    widgetSize.width.toPlatformSize(dm),
                    widgetSize.height.toPlatformSize(dm)
                )
            }
            is WidgetSize.AspectByWidth<out SizeSpec> -> {
                ConstraintLayout.LayoutParams(
                    widgetSize.width.toPlatformSize(dm),
                    0
                ).apply {
                    // TODO test
                    dimensionRatio = widgetSize.aspectRatio.toString()
                }
            }
            is WidgetSize.AspectByHeight<out SizeSpec> -> {
                ConstraintLayout.LayoutParams(
                    0,
                    widgetSize.height.toPlatformSize(dm)
                ).apply {
                    // TODO test
                    dimensionRatio = widgetSize.aspectRatio.toString()
                }
            }
        }
        if (viewBundle.margins != null) {
            lp.applyMargin(dm, viewBundle.margins)
        }
        addView(view, lp)
    }
}

@Suppress("TooManyFunctions")
class ConstraintLayoutConstraintsApi(
    private val constraintLayout: ConstraintLayout,
    private val widgetViewBundle: Map<out Widget<out WidgetSize>, ViewBundle<out WidgetSize>>
) : ConstraintsApi {
    private fun ConstraintItem.view(): View {
        return when (this) {
            ConstraintItem.Root -> constraintLayout
            is ConstraintItem.Child -> widgetViewBundle[this.widget]!!.view // api of widget will not accept this case
            is ConstraintItem.SafeArea -> constraintLayout // TODO WindowInsets support should be implemented
        }
    }

    private fun constraint(
        firstItem: ConstraintItem,
        secondItem: ConstraintItem,
        field: ConstraintLayout.LayoutParams.(Int) -> Unit
    ) {
        val firstView = firstItem.view()
        val secondView = secondItem.view()
        val secondId = if (secondView == constraintLayout) {
            ConstraintLayout.LayoutParams.PARENT_ID
        } else {
            secondView.id
        }

        val clp = firstView.layoutParams as ConstraintLayout.LayoutParams
        clp.field(secondId)
    }

    override fun ConstraintItem.Child.leftToRight(to: ConstraintItem) {
        constraint(this, to) { leftToRight = it }
    }

    override fun ConstraintItem.Child.leftToLeft(to: ConstraintItem) {
        constraint(this, to) { leftToLeft = it }
    }

    override fun ConstraintItem.Child.rightToRight(to: ConstraintItem) {
        constraint(this, to) { rightToRight = it }
    }

    override fun ConstraintItem.Child.rightToLeft(to: ConstraintItem) {
        constraint(this, to) { rightToLeft = it }
    }

    override fun ConstraintItem.Child.topToTop(to: ConstraintItem) {
        constraint(this, to) { topToTop = it }
    }

    override fun ConstraintItem.Child.topToBottom(to: ConstraintItem) {
        constraint(this, to) { topToBottom = it }
    }

    override fun ConstraintItem.Child.centerYToCenterY(to: ConstraintItem) {
        constraint(this, to) { topToTop = it }
        constraint(this, to) { bottomToBottom = it }
    }

    override fun ConstraintItem.Child.centerXToCenterX(to: ConstraintItem) {
        constraint(this, to) { leftToLeft = it }
        constraint(this, to) { rightToRight = it }
    }

    override fun ConstraintItem.Child.bottomToBottom(to: ConstraintItem) {
        constraint(this, to) { bottomToBottom = it }
    }

    override fun ConstraintItem.Child.bottomToTop(to: ConstraintItem) {
        constraint(this, to) { bottomToTop = it }
    }

    override fun ConstraintItem.Child.verticalCenterBetween(
        top: ConstraintItem.VerticalAnchor,
        bottom: ConstraintItem.VerticalAnchor
    ) {
        constraint(this, top.item) {
            when (top.edge) {
                ConstraintItem.VerticalAnchor.Edge.TOP -> topToTop = it
                ConstraintItem.VerticalAnchor.Edge.BOTTOM -> topToBottom = it
            }
        }
        constraint(this, bottom.item) {
            when (bottom.edge) {
                ConstraintItem.VerticalAnchor.Edge.TOP -> bottomToTop = it
                ConstraintItem.VerticalAnchor.Edge.BOTTOM -> bottomToBottom = it
            }
        }
    }

    override fun ConstraintItem.Child.horizontalCenterBetween(
        left: ConstraintItem.HorizontalAnchor,
        right: ConstraintItem.HorizontalAnchor
    ) {
        constraint(this, left.item) {
            when (left.edge) {
                ConstraintItem.HorizontalAnchor.Edge.LEFT -> leftToLeft = it
                ConstraintItem.HorizontalAnchor.Edge.RIGHT -> leftToRight = it
            }
        }
        constraint(this, right.item) {
            when (right.edge) {
                ConstraintItem.HorizontalAnchor.Edge.LEFT -> rightToLeft = it
                ConstraintItem.HorizontalAnchor.Edge.RIGHT -> rightToRight = it
            }
        }
    }

    override fun ConstraintItem.VerticalAnchor.pin(to: ConstraintItem.VerticalAnchor) {
        val firstView = this.item.view()
        val secondView = to.item.view()

        val flp = firstView.layoutParams as ConstraintLayout.LayoutParams

        val sid = secondView.id

        when (this.edge) {
            ConstraintItem.VerticalAnchor.Edge.TOP -> {
                when (to.edge) {
                    ConstraintItem.VerticalAnchor.Edge.TOP -> flp.topToTop = sid
                    ConstraintItem.VerticalAnchor.Edge.BOTTOM -> flp.topToBottom = sid
                }
            }
            ConstraintItem.VerticalAnchor.Edge.BOTTOM -> {
                when (to.edge) {
                    ConstraintItem.VerticalAnchor.Edge.TOP -> flp.bottomToTop = sid
                    ConstraintItem.VerticalAnchor.Edge.BOTTOM -> flp.bottomToBottom = sid
                }
            }
        }
    }

    override fun ConstraintItem.HorizontalAnchor.pin(to: ConstraintItem.HorizontalAnchor) {
        val firstView = this.item.view()
        val secondView = to.item.view()

        val flp = firstView.layoutParams as ConstraintLayout.LayoutParams

        val sid = secondView.id

        when (this.edge) {
            ConstraintItem.HorizontalAnchor.Edge.LEFT -> {
                when (to.edge) {
                    ConstraintItem.HorizontalAnchor.Edge.LEFT -> flp.leftToLeft = sid
                    ConstraintItem.HorizontalAnchor.Edge.RIGHT -> flp.leftToRight = sid
                }
            }
            ConstraintItem.HorizontalAnchor.Edge.RIGHT -> {
                when (to.edge) {
                    ConstraintItem.HorizontalAnchor.Edge.LEFT -> flp.rightToLeft = sid
                    ConstraintItem.HorizontalAnchor.Edge.RIGHT -> flp.rightToRight = sid
                }
            }
        }
    }
}
