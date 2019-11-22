package dev.icerock.moko.widgets.utils

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Direction
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Shape
import dev.icerock.plural.cgColors
import kotlinx.cinterop.useContents
import platform.CoreGraphics.*
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CAGradientLayer
import platform.QuartzCore.CALayer
import platform.UIKit.*


fun Background.caLayer(): CALayer {

    val backgroundLayer: CALayer

    when (fill) {
        is Fill.Solid -> backgroundLayer = CALayer().apply {
            backgroundColor = fill.color.toUIColor().CGColor
        }
        is Fill.Gradient -> {
            backgroundLayer = CAGradientLayer().apply {
                colors = cgColors(fill.colors.map {
                    it.toUIColor()
                })
                println("colors: $colors")

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
        }
        null -> {
            backgroundLayer = CALayer()
        }
    }

    val border = border
    if (border != null) {
        backgroundLayer.borderWidth = border.width.toDouble()
        backgroundLayer.borderColor = border.color.toUIColor().CGColor
    }

    when (val shape = shape) {
        is Shape.Rectangle -> {
            val cornerRadius = shape.cornerRadius
            if (cornerRadius != null) {
                backgroundLayer.cornerRadius = cornerRadius.toDouble()
                backgroundLayer.masksToBounds = true
            }
        }
        is Shape.Oval -> {
            TODO()
        }
    }
    return backgroundLayer
}

