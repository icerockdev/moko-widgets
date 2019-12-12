/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.LinearWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.applySize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.constraints
import platform.UIKit.heightAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.removeConstraint
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor

actual class DefaultLinearWidgetViewFactory actual constructor(
    style: Style
) : DefaultLinearWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: LinearWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackground(style.background)
        }

        fun pm(padding: Float?, margin: Float?): CGFloat {
            return (padding?.toDouble() ?: 0.0) + (margin?.toDouble() ?: 0.0)
        }

        val contentPadding = style.padding

        var lastChildView: UIView? = null
        var lastMargins: MarginValues? = null
        var lastSize: WidgetSize? = null
        widget.children.forEach { childWidget ->
            val childViewBundle = childWidget.buildView(viewController)
            val childView = childViewBundle.view
            childView.translatesAutoresizingMaskIntoConstraints = false

            val childMargins = childViewBundle.margins
            val childSize = childViewBundle.size

            with(container) {
                container.addSubview(childView)

                val lastCV = lastChildView
                val lastCVMargins = lastMargins
                val edges = when (widget.orientation) {
                    Orientation.VERTICAL -> {
                        if (lastCV != null) {
                            childView.topAnchor.constraintEqualToAnchor(
                                anchor = lastCV.bottomAnchor,
                                constant = pm(childMargins?.top, lastCVMargins?.bottom)
                            ).active = true
                        } else {
                            childView.topAnchor.constraintEqualToAnchor(
                                anchor = topAnchor,
                                constant = pm(contentPadding?.top, childMargins?.top)
                            ).active = true
                        }
                        childView.leadingAnchor.constraintEqualToAnchor(
                            anchor = leadingAnchor,
                            constant = pm(contentPadding?.start, childMargins?.start)
                        ).active = true
                        childView.trailingAnchor.constraintEqualToAnchor(
                            anchor = trailingAnchor,
                            constant = -pm(contentPadding?.end, childMargins?.end)
                        ).active = true

                        Edges(
                            top = 0.0,
                            leading = contentPadding?.start?.toDouble() ?: 0.0,
                            trailing = contentPadding?.end?.toDouble() ?: 0.0,
                            bottom = 0.0
                        )
                    }
                    Orientation.HORIZONTAL -> {
                        if (lastCV != null) {
                            childView.leadingAnchor.constraintEqualToAnchor(
                                anchor = lastCV.trailingAnchor,
                                constant = pm(childMargins?.start, lastCVMargins?.end)
                            ).active = true
                        } else {
                            childView.leadingAnchor.constraintEqualToAnchor(
                                anchor = leadingAnchor,
                                constant = pm(childMargins?.start, contentPadding?.start)
                            ).active = true
                        }
                        childView.topAnchor.constraintEqualToAnchor(
                            anchor = topAnchor,
                            constant = pm(childMargins?.top, contentPadding?.top)
                        ).active = true
                        childView.bottomAnchor.constraintLessThanOrEqualToAnchor(
                            anchor = bottomAnchor,
                            constant = -pm(childMargins?.bottom, contentPadding?.bottom)
                        ).active = true

                        Edges(
                            top = contentPadding?.top?.toDouble() ?: 0.0,
                            leading = 0.0,
                            trailing = 0.0,
                            bottom = contentPadding?.bottom?.toDouble() ?: 0.0
                        )
                    }
                }
                corretChildSize(childSize, widget.orientation).also {
                    childView.applySize(
                        size = it,
                        parent = this,
                        edges = edges
                    )
                }
            }

            lastChildView = childView
            lastMargins = childMargins
            lastSize = childSize
        }

        val childView = lastChildView
        val childSize = lastSize
        if (size is WidgetSize.Const<out SizeSpec, out SizeSpec> &&
            childView != null &&
            childSize != null
        ) {

            when (widget.orientation) {
                Orientation.VERTICAL -> {
                    if (size.height == SizeSpec.WrapContent) {
                        container.bottomAnchor.constraintEqualToAnchor(
                            anchor = childView.bottomAnchor,
                            constant = pm(lastMargins?.bottom, contentPadding?.bottom)
                        ).active = true
                    } else if (childSize is WidgetSize.Const<*, *> && childSize.height == SizeSpec.AsParent) {
                        println("as parent")
                        childView.constraints.filterIsInstance<NSLayoutConstraint>()
                            .filter {
                                println("const: $it")
                                (it.firstItem == this && it.firstAnchor == childView.heightAnchor)
                                        || (it.secondItem == this && it.secondAnchor == childView.heightAnchor)
                            }.forEach {
                                println("remove: $it")
                                childView.removeConstraint(it)
                            }

                        container.bottomAnchor.constraintEqualToAnchor(
                            anchor = childView.bottomAnchor,
                            constant = pm(lastMargins?.bottom, contentPadding?.bottom)
                        ).active = true
                    }
                }
                Orientation.HORIZONTAL -> {
                    if (size.width == SizeSpec.WrapContent) {
                        childView.trailingAnchor.constraintEqualToAnchor(
                            anchor = container.trailingAnchor,
                            constant = pm(lastMargins?.end, contentPadding?.end)
                        ).active = true
                    } else if (childSize is WidgetSize.Const<*, *> && childSize.width == SizeSpec.AsParent) {
                        childView.constraints.filterIsInstance<NSLayoutConstraint>()
                            .filter {
                                (it.firstItem == this && it.firstAnchor == childView.widthAnchor)
                                        || (it.secondItem == this && it.secondAnchor == childView.widthAnchor)
                            }.forEach { childView.removeConstraint(it) }

                        childView.trailingAnchor.constraintEqualToAnchor(
                            anchor = container.trailingAnchor,
                            constant = pm(lastMargins?.end, contentPadding?.end)
                        ).active = true
                    }
                }
            }
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = style.margins
        )
    }

    private fun corretChildSize(size: WidgetSize, orientation: Orientation): WidgetSize {
        //TODO: Support aspects correcting?

        var result = size
        when (size) {
            is WidgetSize.Const<*, *> -> {
                if (size.width == SizeSpec.AsParent && orientation == Orientation.HORIZONTAL) {
                    result = WidgetSize.Const(SizeSpec.WrapContent, size.height)
                }
                if (size.height == SizeSpec.AsParent && orientation == Orientation.VERTICAL) {
                    result = WidgetSize.Const(size.width, SizeSpec.WrapContent)
                }
            }
        }
        return result
    }
}
