/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Direction
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Shape
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.plural.cgColors
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CAGradientLayer
import platform.UIKit.NSLayoutDimension
import platform.UIKit.UIControl
import platform.UIKit.UIEdgeInsets
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIView
import platform.UIKit.backgroundColor
import platform.UIKit.bottomAnchor
import platform.UIKit.heightAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.widthAnchor

fun Widget.getSize(): WidgetSize? {
    return ((this as? Styled<*>)?.style as? Sized)?.size
}

fun Widget.getMargins(): MarginValues? {
    return ((this as? Styled<*>)?.style as? Margined)?.margins
}

fun Widget.getPaddings(): PaddingValues? {
    return ((this as? Styled<*>)?.style as? Padded)?.padding
}

fun layoutWidget(
    rootWidget: Widget,
    rootView: UIView,
    childWidget: Widget,
    childView: UIView
): Edges<CGFloat> {
    val containerPadding = rootWidget.getPaddings()

    val childSize = childWidget.getSize()
    val childMargins = childWidget.getMargins()

    val edges: Edges<CGFloat> = containerPadding + childMargins

    if (childSize != null) {
        childView.applySize(childSize, rootView, edges)
    }

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
        constant = edges.trailing
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
        constSize: Int,
        edgeSum: CGFloat
    ) {
        when (constSize) {
            SizeSpec.AS_PARENT -> {
                myAnchor.constraintEqualToAnchor(parentAnchor, constant = -edgeSum).active = true
            }
            SizeSpec.WRAP_CONTENT -> {
                // nothing (intristic size by default)
            }
            else -> myAnchor.constraintEqualToConstant(constSize.toDouble()).active = true
        }
    }

    when (size) {
        is WidgetSize.Const -> {
            applyToDimension(
                widthAnchor,
                parent.widthAnchor,
                size.width,
                edgeSum = edges.trailing + edges.leading
            )
            applyToDimension(
                heightAnchor,
                parent.heightAnchor,
                size.height,
                edgeSum = edges.top + edges.bottom
            )
        }
        is WidgetSize.AspectByWidth -> {
            applyToDimension(
                widthAnchor,
                parent.widthAnchor,
                size.width,
                edgeSum = edges.trailing + edges.leading
            )
            heightAnchor.constraintEqualToAnchor(widthAnchor, 1 / size.aspectRatio.toDouble())
                .active = true
        }
        is WidgetSize.AspectByHeight -> {
            applyToDimension(
                heightAnchor,
                parent.heightAnchor,
                size.height,
                edgeSum = edges.top + edges.bottom
            )
            widthAnchor.constraintEqualToAnchor(heightAnchor, size.aspectRatio.toDouble()).active =
                true
        }
    }
}

fun UIView.applyBackground(background: Background?) {
    if (background == null) return

    when (val fill = background.fill) {
        is Fill.Solid -> backgroundColor = fill.color.toUIColor()
        is Fill.Gradient -> {
            val gradientLayer = CAGradientLayer().apply {
                colors = cgColors(fill.colors.map {
                    it.toUIColor()
                })
                println("colors: $colors")

                frame = layer.bounds
                masksToBounds = true

                val (start, end) = when (fill.direction) {
                    Direction.LEFT_RIGHT -> CGPointMake(0.0, 0.5) to CGPointMake(1.0, 0.5)
                    Direction.RIGHT_LEFT -> CGPointMake(1.0, 0.5) to CGPointMake(0.0, 0.5)
                    Direction.TOP_BOTTOM -> CGPointMake(0.5, 0.0) to CGPointMake(0.5, 1.0)
                    Direction.BOTTOM_TOP -> CGPointMake(0.5, 1.0) to CGPointMake(0.5, 0.0)
                    Direction.BL_TR -> CGPointMake(0.0, 1.0) to CGPointMake(1.0, 0.0)
                    Direction.BR_TL -> CGPointMake(1.0, 1.0) to CGPointMake(0.0, 0.0)
                    Direction.TR_BL -> CGPointMake(1.0, 0.0) to CGPointMake(0.0, 1.0)
                    Direction.TL_BR -> CGPointMake(0.0, 0.0) to CGPointMake(1.0, 1.0)
                }

                startPoint = start
                endPoint = end
            }
            layer.insertSublayer(gradientLayer, atIndex = 0)

            // FIXME memoryleak.
            layer.displayLink {
                val (width, height) = layer.bounds.useContents { size.width to size.height }
                gradientLayer.frame = CGRectMake(0.0, 0.0, width, height)
            }
        }
    }

    val border = background.border
    if (border != null) {
        layer.borderWidth = border.width.toDouble()
        layer.borderColor = border.color.toUIColor().CGColor
    }

    when (val shape = background.shape) {
        is Shape.Rectangle -> {
            val cornerRadius = shape.cornerRadius
            if (cornerRadius != null) {
                layer.cornerRadius = cornerRadius.toDouble()
                layer.masksToBounds = true
            }
        }
        is Shape.Oval -> {
            TODO()
        }
    }
}

fun UIControl.applyBackground(stateBackground: StateBackground?) {
    if (stateBackground == null) return

    applyBackground(stateBackground.normal)
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