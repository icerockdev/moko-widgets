/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.applySize
import dev.icerock.moko.widgets.utils.getMargins
import dev.icerock.moko.widgets.utils.getSize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.constraints
import platform.UIKit.heightAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.removeConstraint
import platform.UIKit.superview
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor

actual var linearWidgetViewFactory: VFC<LinearWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

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
    widget.children.forEach { childWidget ->
        val childView = childWidget.buildView(viewController)
        childView.translatesAutoresizingMaskIntoConstraints = false

        val childSize = childWidget.getSize()
        val childMargins = childWidget.getMargins()

        with(container) {
            container.addSubview(childView)

            val lastCV = lastChildView
            val lastCVMargins = lastMargins
            val edges = when (style.orientation) {
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
            childSize?.let { corretChildSize(it, style.orientation) }?.also {
                childView.applySize(
                    size = it,
                    parent = this,
                    edges = edges
                )
            }
        }

        lastChildView = childView
        lastMargins = childMargins
    }

    if (style.size is WidgetSize.Const) {
        lastChildView?.run {
            val lastWidgetSize = widget.children.last().getSize()!!

            when (style.orientation) {
                Orientation.VERTICAL -> {
                    if (style.size.height == SizeSpec.WRAP_CONTENT) {
                        container.bottomAnchor.constraintEqualToAnchor(
                            anchor = bottomAnchor,
                            constant = pm(lastMargins?.bottom, contentPadding?.bottom)
                        ).active = true
                    } else if (lastWidgetSize is WidgetSize.Const && lastWidgetSize.height == SizeSpec.AS_PARENT) {
                        println("as parent")
                        this.constraints.filterIsInstance<NSLayoutConstraint>()
                            .filter {
                                println("const: $it")
                                (it.firstItem == this && it.firstAnchor == this.heightAnchor)
                                        || (it.secondItem == this && it.secondAnchor == this.heightAnchor)
                            }.forEach {
                                println("remove: $it")
                                this.removeConstraint(it)
                            }

                        container.bottomAnchor.constraintEqualToAnchor(
                            anchor = bottomAnchor,
                            constant = pm(lastMargins?.bottom, contentPadding?.bottom)
                        ).active = true
                    }
                }
                Orientation.HORIZONTAL -> {
                    if (style.size.width == SizeSpec.WRAP_CONTENT) {
                        trailingAnchor.constraintEqualToAnchor(
                            anchor = container.trailingAnchor,
                            constant = pm(lastMargins?.end, contentPadding?.end)
                        ).active = true
                    } else if (lastWidgetSize is WidgetSize.Const && lastWidgetSize.width == SizeSpec.AS_PARENT) {
                        this.constraints.filterIsInstance<NSLayoutConstraint>()
                            .filter {
                                (it.firstItem == this && it.firstAnchor == this.widthAnchor)
                                        || (it.secondItem == this && it.secondAnchor == this.widthAnchor)
                            }.forEach { this.removeConstraint(it) }

                        trailingAnchor.constraintEqualToAnchor(
                            anchor = container.trailingAnchor,
                            constant = pm(lastMargins?.end, contentPadding?.end)
                        ).active = true
                    }
                }
            }
        }
    }

    container
}

private fun corretChildSize(size: WidgetSize, orientation: Orientation): WidgetSize {
    //TODO: Support aspects correcting?

    var result = size
    when (size) {
        is WidgetSize.Const -> {
            if (size.width == SizeSpec.AS_PARENT && orientation == Orientation.HORIZONTAL) {
                result = WidgetSize.Const(SizeSpec.WRAP_CONTENT, size.height)
            }
            if (size.height == SizeSpec.AS_PARENT && orientation == Orientation.VERTICAL) {
                result = WidgetSize.Const(size.width, SizeSpec.WRAP_CONTENT)
            }
        }
    }
    return result
}