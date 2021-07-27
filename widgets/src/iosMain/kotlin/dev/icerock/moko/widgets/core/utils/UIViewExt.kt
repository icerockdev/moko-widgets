/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.NSLayoutDimension
import platform.UIKit.UIEdgeInsets
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UILayoutConstraintAxis
import platform.UIKit.UILayoutConstraintAxisHorizontal
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UILayoutFittingCompressedSize
import platform.UIKit.UILayoutPriorityDefaultLow
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.bottomAnchor
import platform.UIKit.heightAnchor
import platform.UIKit.layoutSubviews
import platform.UIKit.leadingAnchor
import platform.UIKit.setContentCompressionResistancePriority
import platform.UIKit.systemLayoutSizeFittingSize
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.updateConstraints
import platform.UIKit.widthAnchor

fun applySizeToChild(
    rootView: UIView,
    rootPadding: PaddingValues?,
    childView: UIView,
    childSize: WidgetSize,
    childMargins: MarginValues?
): Edges<CGFloat> {
    val edges: Edges<CGFloat> = rootPadding + childMargins

    childView.applySize(childSize, rootView, edges)

    return edges
}

fun UIView.fillChildView(childView: UIView, edges: Edges<CGFloat>) {
    childView.topAnchor.constraintEqualToAnchor(
        anchor = topAnchor,
        constant = edges.top
    ).active = true
    childView.leadingAnchor.constraintEqualToAnchor(
        anchor = leadingAnchor,
        constant = edges.leading
    ).active = true
    childView.trailingAnchor.constraintEqualToAnchor(
        anchor = trailingAnchor,
        constant = -edges.trailing
    ).active = true
    childView.bottomAnchor.constraintEqualToAnchor(
        anchor = bottomAnchor,
        constant = edges.bottom
    ).active = true
}

fun UIView.applySize(size: WidgetSize, parent: UIView, edges: Edges<CGFloat>) {
    fun applyToDimension(
        myAnchor: NSLayoutDimension,
        parentAnchor: NSLayoutDimension,
        constSize: SizeSpec,
        edgeSum: CGFloat,
        axis: UILayoutConstraintAxis
    ) {
        when (constSize) {
            SizeSpec.AsParent -> {
                myAnchor.constraintEqualToAnchor(parentAnchor, constant = -edgeSum).active = true
            }
            SizeSpec.WrapContent -> {
                this.setContentCompressionResistancePriority(priority = 999f, forAxis = axis)
            }
            is SizeSpec.Exact -> {
                myAnchor.constraintEqualToConstant(constSize.points.toDouble()).active = true
            }
            SizeSpec.MatchConstraint -> {
                // nothing - constraints should set size
            }
        }
    }

    when (size) {
        is WidgetSize.Const<out SizeSpec, out SizeSpec> -> {
            applyToDimension(
                widthAnchor,
                parent.widthAnchor,
                size.width,
                edgeSum = edges.trailing + edges.leading,
                axis = UILayoutConstraintAxisHorizontal
            )
            applyToDimension(
                heightAnchor,
                parent.heightAnchor,
                size.height,
                edgeSum = edges.top + edges.bottom,
                axis = UILayoutConstraintAxisVertical
            )
        }
        is WidgetSize.AspectByWidth<out SizeSpec> -> {
            applyToDimension(
                widthAnchor,
                parent.widthAnchor,
                size.width,
                edgeSum = edges.trailing + edges.leading,
                axis = UILayoutConstraintAxisHorizontal
            )
            heightAnchor.constraintEqualToAnchor(widthAnchor, 1 / size.aspectRatio.toDouble())
                .active = true
        }
        is WidgetSize.AspectByHeight<out SizeSpec> -> {
            applyToDimension(
                heightAnchor,
                parent.heightAnchor,
                size.height,
                edgeSum = edges.top + edges.bottom,
                axis = UILayoutConstraintAxisVertical
            )
            widthAnchor.constraintEqualToAnchor(heightAnchor, size.aspectRatio.toDouble()).active =
                true
        }
    }
}

data class Edges<T>(
    val top: T,
    val leading: T,
    val trailing: T,
    val bottom: T
)

operator fun PaddingValues?.plus(marginValues: MarginValues?): Edges<CGFloat> {
    fun paddingWithMargin(padding: Float?, margin: Float?): CGFloat {
        return (padding?.toDouble() ?: 0.0) + (margin?.toDouble() ?: 0.0)
    }

    return Edges(
        top = paddingWithMargin(this?.top, marginValues?.top),
        leading = paddingWithMargin(this?.start, marginValues?.start),
        bottom = paddingWithMargin(this?.bottom, marginValues?.bottom),
        trailing = paddingWithMargin(this?.end, marginValues?.end)
    )
}

fun PaddingValues.toEdgeInsets(): CValue<UIEdgeInsets> {
    return UIEdgeInsetsMake(
        top = top.toDouble(),
        left = start.toDouble(),
        bottom = bottom.toDouble(),
        right = end.toDouble()
    )
}

@Suppress("MagicNumber")
fun UIView.wrapContentHeight(width: CGFloat? = null): CGFloat {
    var fittingSize = CGSizeMake(width ?: 2000.0, UILayoutFittingCompressedSize.height)

    println("Trying to calculate height by content width: ${fittingSize.useContents { width }}")

    updateConstraints()
    layoutSubviews()

    val resultSize = systemLayoutSizeFittingSize(
        fittingSize,
        withHorizontalFittingPriority = UILayoutPriorityRequired,
        verticalFittingPriority = UILayoutPriorityDefaultLow
    ).useContents { this }
    println("Calculate wrap content size: W: ${resultSize.width} H: ${resultSize.height}")
    return resultSize.height
}

fun UIView.wrapContentWidth(height: CGFloat? = null): CGFloat {
    val expandedFrame = CGRectMake(
        0.0,
        0.0,
        UIScreen.mainScreen.bounds.useContents { size.width },
        height ?: UIScreen.mainScreen.bounds.useContents { size.height }
    )
    println("== Trying to calculate height with from initial size: " +
            "W: ${expandedFrame.useContents { size }.width} " +
            "H:  ${expandedFrame.useContents { size }.height} ==")
    height?.let {
        heightAnchor.constraintEqualToConstant(it).setActive(true)
    }
    updateConstraints()
    layoutSubviews()
    val resultSize = systemLayoutSizeFittingSize(
        expandedFrame.useContents { size }.readValue(),
        withHorizontalFittingPriority = UILayoutPriorityDefaultLow,
        verticalFittingPriority = UILayoutPriorityRequired
    ).useContents { this }
    println("== Calculate wrap content size: W: ${resultSize.width} H: ${resultSize.height} ==")
    return resultSize.width
}
