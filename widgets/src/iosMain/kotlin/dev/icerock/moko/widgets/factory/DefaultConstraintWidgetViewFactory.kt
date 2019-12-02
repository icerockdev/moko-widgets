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
import dev.icerock.moko.widgets.style.view.WidgetSize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIView
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
        }

        val widgetViewBundle: Map<out Widget<out WidgetSize>, ViewBundle<out WidgetSize>> =
            widget.children.associateWith { childWidget ->
                val viewBundle = childWidget.buildView(viewFactoryContext)
                viewBundle.view.translatesAutoresizingMaskIntoConstraints = false

                // TODO apply size?

                container.addSubview(viewBundle.view)

                viewBundle
            }

        fun ConstraintItem.view(): UIView {
            return when (this) {
                ConstraintItem.Root -> container
                is ConstraintItem.Child -> widgetViewBundle[this.widget]!!.view // api of widget will not accept this case
            }
        }

        val constraintsHandler = object : ConstraintsApi {
            private fun constraint(
                firstItem: ConstraintItem, secondItem: ConstraintItem,
                block: UIView.(UIView) -> NSLayoutConstraint
            ) {
                val firstView = firstItem.view()
                val secondView = secondItem.view()

                // TODO margins + padding apply
                block(firstView, secondView).active = true
            }

            override fun ConstraintItem.Child.leftToRight(to: ConstraintItem) {
                constraint(this, to) { leftAnchor.constraintEqualToAnchor(it.rightAnchor) }
            }

            override fun ConstraintItem.Child.leftToLeft(to: ConstraintItem) {
                constraint(this, to) { leftAnchor.constraintEqualToAnchor(it.leftAnchor) }
            }

            override fun ConstraintItem.Child.rightToRight(to: ConstraintItem) {
                constraint(this, to) { rightAnchor.constraintEqualToAnchor(it.rightAnchor) }
            }

            override fun ConstraintItem.Child.rightToLeft(to: ConstraintItem) {
                constraint(this, to) { rightAnchor.constraintEqualToAnchor(it.leftAnchor) }
            }

            override fun ConstraintItem.Child.topToTop(to: ConstraintItem) {
                constraint(this, to) { topAnchor.constraintEqualToAnchor(it.topAnchor) }
            }

            override fun ConstraintItem.Child.topToBottom(to: ConstraintItem) {
                constraint(this, to) { topAnchor.constraintEqualToAnchor(it.bottomAnchor) }
            }

            override fun ConstraintItem.Child.centerYToCenterY(to: ConstraintItem) {
                constraint(this, to) { centerYAnchor.constraintEqualToAnchor(it.centerYAnchor) }
            }

            override fun ConstraintItem.Child.centerXToCenterX(to: ConstraintItem) {
                constraint(this, to) { centerXAnchor.constraintEqualToAnchor(it.centerXAnchor) }
            }

            override fun ConstraintItem.Child.bottomToBottom(to: ConstraintItem) {
                constraint(this, to) { bottomAnchor.constraintEqualToAnchor(it.bottomAnchor) }
            }

            override fun ConstraintItem.Child.bottomToTop(to: ConstraintItem) {
                constraint(this, to) { bottomAnchor.constraintEqualToAnchor(it.topAnchor) }
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
