/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import dev.icerock.moko.widgets.Constraint
import dev.icerock.moko.widgets.ConstraintItem
import dev.icerock.moko.widgets.ConstraintWidget
import dev.icerock.moko.widgets.ConstraintsApi
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.dp

actual class ConstraintViewFactory actual constructor(
    private val background: Background?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<ConstraintWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ConstraintWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context

        val constraintLayout = ConstraintLayout(context)
        constraintLayout.applyBackgroundIfNeeded(background)
        constraintLayout.applyPaddingIfNeeded(padding)

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
            margins = margins
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

    override fun ConstraintItem.Child.leftToRight(to: ConstraintItem): Constraint {
        constraint(this, to) { leftToRight = it }
        return ConstraintImpl(this) { leftMargin = it }
    }

    override fun ConstraintItem.Child.leftToLeft(to: ConstraintItem): Constraint {
        constraint(this, to) { leftToLeft = it }
        return ConstraintImpl(this) { leftMargin = it }
    }

    override fun ConstraintItem.Child.rightToRight(to: ConstraintItem): Constraint {
        constraint(this, to) { rightToRight = it }
        return ConstraintImpl(this) { rightMargin = it }
    }

    override fun ConstraintItem.Child.rightToLeft(to: ConstraintItem): Constraint {
        constraint(this, to) { rightToLeft = it }
        return ConstraintImpl(this) { rightMargin = it }
    }

    override fun ConstraintItem.Child.topToTop(to: ConstraintItem): Constraint {
        constraint(this, to) { topToTop = it }
        return ConstraintImpl(this) { topMargin = it }
    }

    override fun ConstraintItem.Child.topToBottom(to: ConstraintItem): Constraint {
        constraint(this, to) { topToBottom = it }
        return ConstraintImpl(this) { topMargin = it }
    }

    override fun ConstraintItem.Child.centerYToCenterY(to: ConstraintItem) {
        constraint(this, to) { topToTop = it }
        constraint(this, to) { bottomToBottom = it }
    }

    override fun ConstraintItem.Child.centerXToCenterX(to: ConstraintItem) {
        constraint(this, to) { leftToLeft = it }
        constraint(this, to) { rightToRight = it }
    }

    override fun ConstraintItem.Child.bottomToBottom(to: ConstraintItem): Constraint {
        constraint(this, to) { bottomToBottom = it }
        return ConstraintImpl(this) { bottomMargin = it }
    }

    override fun ConstraintItem.Child.bottomToTop(to: ConstraintItem): Constraint {
        constraint(this, to) { bottomToTop = it }
        return ConstraintImpl(this) { bottomMargin = it }
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

    override fun ConstraintItem.VerticalAnchor.pin(to: ConstraintItem.VerticalAnchor): Constraint {
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

        return ConstraintImpl(
            item = this.item,
            marginSetter = {
                when (this@pin.edge) {
                    ConstraintItem.VerticalAnchor.Edge.TOP -> {
                        topMargin = it
                    }
                    ConstraintItem.VerticalAnchor.Edge.BOTTOM -> {
                        bottomMargin = it
                    }
                }
            }
        )
    }

    override fun ConstraintItem.HorizontalAnchor.pin(to: ConstraintItem.HorizontalAnchor): Constraint {
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

        return ConstraintImpl(
            item = this.item,
            marginSetter = {
                when (this@pin.edge) {
                    ConstraintItem.HorizontalAnchor.Edge.LEFT -> {
                        leftMargin = it
                    }
                    ConstraintItem.HorizontalAnchor.Edge.RIGHT -> {
                        rightMargin = it
                    }
                }
            }
        )
    }

    private inner class ConstraintImpl(
        val view: View,
        val marginSetter: ViewGroup.MarginLayoutParams.(Int) -> Unit
    ) : Constraint {
        constructor(
            item: ConstraintItem,
            marginSetter: ViewGroup.MarginLayoutParams.(Int) -> Unit
        ) : this(view = item.view(), marginSetter = marginSetter)

        override fun offset(points: Int) {
            with(view.layoutParams as ViewGroup.MarginLayoutParams) {
                this.marginSetter(points.dp(view.context))
            }
        }
    }
}
