/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.LinearWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
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

actual class LinearViewFactory actual constructor(
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<out Fill>?
) : ViewFactory<LinearWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: LinearWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(background)
        }

        when (widget.orientation) {
            Orientation.HORIZONTAL -> layoutHorizontal(
                root = container,
                children = widget.children,
                viewController = viewController,
                size = size
            )
            Orientation.VERTICAL -> layoutVertical(
                root = container,
                children = widget.children,
                viewController = viewController,
                size = size
            )
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = margins
        )
    }

    private fun correctChildSize(size: WidgetSize, orientation: Orientation): WidgetSize {
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

    private fun pm(padding: Float?, margin: Float?): CGFloat {
        return (padding?.toDouble() ?: 0.0) + (margin?.toDouble() ?: 0.0)
    }

    private fun layoutHorizontal(
        root: UIView,
        children: List<Widget<out WidgetSize>>,
        viewController: UIViewController,
        size: WidgetSize
    ) {
        var lastChildView: UIView? = null
        var lastMargins: MarginValues? = null
        var lastSize: WidgetSize? = null

        children.forEach { childWidget ->
            val (childView, childSize, childMargins) = childWidget.buildView(viewController)

            childView.translatesAutoresizingMaskIntoConstraints = false

            with(root) {
                addSubview(childView)

                val lastCV = lastChildView
                val lastCVMargins = lastMargins

                if (lastCV != null) {
                    childView.leadingAnchor.constraintEqualToAnchor(
                        anchor = lastCV.trailingAnchor,
                        constant = pm(childMargins?.start, lastCVMargins?.end)
                    ).active = true
                } else {
                    childView.leadingAnchor.constraintEqualToAnchor(
                        anchor = leadingAnchor,
                        constant = pm(childMargins?.start, padding?.start)
                    ).active = true
                }

                val topOffset = pm(childMargins?.top, padding?.top)
                val bottomOffset = pm(childMargins?.bottom, padding?.bottom)

                childView.topAnchor.constraintEqualToAnchor(
                    anchor = topAnchor,
                    constant = topOffset
                ).active = true
                bottomAnchor.constraintLessThanOrEqualToAnchor(
                    anchor = childView.bottomAnchor,
                    constant = bottomOffset
                ).active = true

                val edges = Edges(
                    top = topOffset,
                    leading = 0.0,
                    trailing = 0.0,
                    bottom = bottomOffset
                )

                correctChildSize(childSize, Orientation.HORIZONTAL).also {
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
        if (size is WidgetSize.Const<out SizeSpec, out SizeSpec> && childView != null && childSize != null) {
            if (size.width == SizeSpec.WrapContent) {
                childView.trailingAnchor.constraintEqualToAnchor(
                    anchor = root.trailingAnchor,
                    constant = pm(lastMargins?.end, padding?.end)
                ).active = true
            } else if (childSize is WidgetSize.Const<*, *> && childSize.width == SizeSpec.AsParent) {
                childView.constraints.filterIsInstance<NSLayoutConstraint>()
                    .filter {
                        (it.firstItem == this && it.firstAnchor == childView.widthAnchor)
                                || (it.secondItem == this && it.secondAnchor == childView.widthAnchor)
                    }.forEach { childView.removeConstraint(it) }

                childView.trailingAnchor.constraintEqualToAnchor(
                    anchor = root.trailingAnchor,
                    constant = pm(lastMargins?.end, padding?.end)
                ).active = true
            }
        }
    }

    private fun layoutVertical(
        root: UIView,
        children: List<Widget<out WidgetSize>>,
        viewController: UIViewController,
        size: WidgetSize
    ) {
        var lastChildView: UIView? = null
        var lastMargins: MarginValues? = null
        var lastSize: WidgetSize? = null

        children.forEach { childWidget ->
            val childViewBundle = childWidget.buildView(viewController)
            val childView = childViewBundle.view
            childView.translatesAutoresizingMaskIntoConstraints = false

            val childMargins = childViewBundle.margins
            val childSize = childViewBundle.size

            with(root) {
                addSubview(childView)

                val lastCV = lastChildView
                val lastCVMargins = lastMargins

                if (lastCV != null) {
                    childView.topAnchor.constraintEqualToAnchor(
                        anchor = lastCV.bottomAnchor,
                        constant = pm(childMargins?.top, lastCVMargins?.bottom)
                    ).active = true
                } else {
                    childView.topAnchor.constraintEqualToAnchor(
                        anchor = topAnchor,
                        constant = pm(padding?.top, childMargins?.top)
                    ).active = true
                }

                val leadingOffset = pm(padding?.start, childMargins?.start)
                val trailingOffset = pm(padding?.end, childMargins?.end)

                childView.leadingAnchor.constraintEqualToAnchor(
                    anchor = leadingAnchor,
                    constant = leadingOffset
                ).active = true
                trailingAnchor.constraintEqualToAnchor(
                    anchor = childView.trailingAnchor,
                    constant = trailingOffset
                ).active = true

                val edges = Edges(
                    top = 0.0,
                    leading = leadingOffset,
                    trailing = trailingOffset,
                    bottom = 0.0
                )

                correctChildSize(childSize, Orientation.VERTICAL).also {
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

            if (size.height == SizeSpec.WrapContent) {
                root.bottomAnchor.constraintEqualToAnchor(
                    anchor = childView.bottomAnchor,
                    constant = pm(lastMargins?.bottom, padding?.bottom)
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

                root.bottomAnchor.constraintEqualToAnchor(
                    anchor = childView.bottomAnchor,
                    constant = pm(lastMargins?.bottom, padding?.bottom)
                ).active = true
            }

        }
    }
}
