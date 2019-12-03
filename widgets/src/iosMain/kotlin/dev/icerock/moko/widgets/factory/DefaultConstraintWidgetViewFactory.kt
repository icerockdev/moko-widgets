/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ConstraintItem
import dev.icerock.moko.widgets.ConstraintWidget
import dev.icerock.moko.widgets.ConstraintsApi
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.applySizeToChild
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSLayoutAnchor
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UILayoutGuide
import platform.UIKit.UIView
import platform.UIKit.addLayoutGuide
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.leftAnchor
import platform.UIKit.rightAnchor
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class DefaultConstraintWidgetViewFactory actual constructor(
    style: Style
) : DefaultConstraintWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ConstraintWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyBackground(style.background)
        }

        val widgetViewBundle: Map<out Widget<out WidgetSize>, ViewBundle<out WidgetSize>> =
            widget.children.associateWith { childWidget ->
                val viewBundle = childWidget.buildView(viewFactoryContext)

                val childView = viewBundle.view

                container.addSubview(childView)

                applySizeToChild(
                    rootView = container,
                    rootPadding = style.padding,
                    childView = childView,
                    childSize = viewBundle.size,
                    childMargins = viewBundle.margins
                )

                viewBundle
            }

        fun ConstraintItem.view(): UIView {
            return when (this) {
                ConstraintItem.Root -> container
                // api of widget will not accept case of null
                is ConstraintItem.Child -> widgetViewBundle[this.widget]!!.view
            }
        }

        fun ConstraintItem.Child.viewBundle(): ViewBundle<out WidgetSize> {
            return widgetViewBundle[this.widget]!! // api of widget will not accept case of null
        }

        val constraintsHandler = object : ConstraintsApi {
            private fun ConstraintItem.Child.calcConstant(
                to: ConstraintItem,
                marginGetter: MarginValues.() -> Float,
                paddingGetter: PaddingValues.() -> Float
            ): CGFloat {
                var const: CGFloat = viewBundle().margins?.let(marginGetter)?.toDouble() ?: 0.0
                if (to is ConstraintItem.Root) const += style.padding?.let(paddingGetter)?.toDouble() ?: 0.0
                return const
            }

            private fun constraint(
                firstItem: ConstraintItem.Child,
                secondItem: ConstraintItem,
                firstMargin: MarginValues.() -> Float = { 0.0f },
                secondPadding: PaddingValues.() -> Float = { 0.0f },
                block: UIView.(UIView) -> NSLayoutConstraint
            ) {
                val firstView = firstItem.view()
                val secondView = secondItem.view()

                val const = firstItem.calcConstant(
                    secondItem,
                    marginGetter = firstMargin,
                    paddingGetter = secondPadding
                )

                // TODO margins + padding apply
                block(firstView, secondView).apply {
                    constant = const
                    active = true
                }
            }

            override fun ConstraintItem.Child.leftToRight(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.start },
                    secondPadding = { this.end },
                    block = { leftAnchor.constraintEqualToAnchor(it.rightAnchor) }
                )
            }

            override fun ConstraintItem.Child.leftToLeft(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.start },
                    secondPadding = { this.start },
                    block = { leftAnchor.constraintEqualToAnchor(it.leftAnchor) }
                )
            }

            override fun ConstraintItem.Child.rightToRight(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.end },
                    secondPadding = { this.end },
                    block = { it.rightAnchor.constraintEqualToAnchor(rightAnchor) }
                )
            }

            override fun ConstraintItem.Child.rightToLeft(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.end },
                    secondPadding = { this.start },
                    block = { it.leftAnchor.constraintEqualToAnchor(rightAnchor) }
                )
            }

            override fun ConstraintItem.Child.topToTop(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.top },
                    secondPadding = { this.top },
                    block = { topAnchor.constraintEqualToAnchor(it.topAnchor) }
                )
            }

            override fun ConstraintItem.Child.topToBottom(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.top },
                    secondPadding = { this.bottom },
                    block = { topAnchor.constraintEqualToAnchor(it.bottomAnchor) }
                )
            }

            override fun ConstraintItem.Child.bottomToBottom(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.bottom },
                    secondPadding = { this.bottom },
                    block = { it.bottomAnchor.constraintEqualToAnchor(bottomAnchor) }
                )
            }

            override fun ConstraintItem.Child.bottomToTop(to: ConstraintItem) {
                constraint(
                    firstItem = this,
                    secondItem = to,
                    firstMargin = { this.bottom },
                    secondPadding = { this.top },
                    block = { it.topAnchor.constraintEqualToAnchor(bottomAnchor) }
                )
            }

            override fun ConstraintItem.Child.centerYToCenterY(to: ConstraintItem) {
                constraint(this, to) { centerYAnchor.constraintEqualToAnchor(it.centerYAnchor) }
            }

            override fun ConstraintItem.Child.centerXToCenterX(to: ConstraintItem) {
                constraint(this, to) { centerXAnchor.constraintEqualToAnchor(it.centerXAnchor) }
            }

            override fun ConstraintItem.Child.verticalCenterBetween(
                top: ConstraintItem.VerticalAnchor,
                bottom: ConstraintItem.VerticalAnchor
            ) {
                val layoutGuide = UILayoutGuide()
                container.addLayoutGuide(layoutGuide)

                val topView = top.item.view()
                val bottomView = bottom.item.view()
                layoutGuide.topAnchor.constraintEqualToAnchor(top.edge.toAnchor(topView)).active = true
                layoutGuide.bottomAnchor.constraintEqualToAnchor(bottom.edge.toAnchor(bottomView)).active = true

                val view = this.view()
                view.centerYAnchor.constraintEqualToAnchor(layoutGuide.centerYAnchor).active = true
            }

            private fun ConstraintItem.VerticalAnchor.Edge.toAnchor(view: UIView): NSLayoutAnchor {
                return when (this) {
                    ConstraintItem.VerticalAnchor.Edge.TOP -> view.topAnchor
                    ConstraintItem.VerticalAnchor.Edge.BOTTOM -> view.bottomAnchor
                }
            }

            override fun ConstraintItem.Child.horizontalCenterBetween(
                left: ConstraintItem.HorizontalAnchor,
                right: ConstraintItem.HorizontalAnchor
            ) {
                val layoutGuide = UILayoutGuide()
                container.addLayoutGuide(layoutGuide)

                val leftView = left.item.view()
                val rightView = right.item.view()
                layoutGuide.leftAnchor.constraintEqualToAnchor(left.edge.toAnchor(leftView)).active = true
                layoutGuide.rightAnchor.constraintEqualToAnchor(right.edge.toAnchor(rightView)).active = true

                val view = this.view()
                view.centerXAnchor.constraintEqualToAnchor(layoutGuide.centerXAnchor).active = true
            }

            private fun ConstraintItem.HorizontalAnchor.Edge.toAnchor(view: UIView): NSLayoutAnchor {
                return when (this) {
                    ConstraintItem.HorizontalAnchor.Edge.LEFT -> view.leftAnchor
                    ConstraintItem.HorizontalAnchor.Edge.RIGHT -> view.rightAnchor
                }
            }
        }

        widget.constraints.invoke(constraintsHandler)

        return ViewBundle(
            view = container,
            size = size,
            margins = style.margins
        )
    }
}
