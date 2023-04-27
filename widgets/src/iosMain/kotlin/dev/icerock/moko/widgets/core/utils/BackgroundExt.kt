/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Direction
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.PressableState
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CAGradientLayer
import platform.QuartzCore.CALayer
import platform.QuartzCore.CATransaction
import platform.UIKit.UIButton
import platform.UIKit.UIColor
import platform.UIKit.UIControl
import platform.UIKit.UIView
import platform.UIKit.adjustsImageWhenDisabled
import platform.UIKit.adjustsImageWhenHighlighted
import platform.UIKit.backgroundColor

@Suppress("MagicNumber", "ComplexMethod")
fun Background<out Fill>.caLayer(): CALayer {

    val backgroundLayer: CALayer

    when (val fill = this.fill) {
        is Fill.Solid -> backgroundLayer = CALayer().apply {
            backgroundColor = fill.color.toUIColor().CGColor
        }

        is Fill.Gradient -> {
            backgroundLayer = CAGradientLayer().apply {
                colors = cgColors(fill.colors.map {
                    it.toUIColor()
                })

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

    border?.also {
        backgroundLayer.borderWidth = it.width.toDouble()
        backgroundLayer.borderColor = it.color.toUIColor().CGColor
    }
    cornerRadius?.also {
        backgroundLayer.cornerRadius = it.toDouble()
    }

    maskedCorners?.also { cornersList ->
        backgroundLayer.maskedCorners = cornersList.toCACornerMask()
    }

    return backgroundLayer
}

fun UIButton.applyStateBackgroundIfNeeded(background: PressableState<Background<out Fill>>?) {
    if (background == null) return

    adjustsImageWhenDisabled = false
    adjustsImageWhenHighlighted = false

    val stateLayers = StateLayers(
        normal = background.normal.caLayer().also {
            layer.addSublayer(it)
        },
        disabled = background.disabled.caLayer().also {
            layer.addSublayer(it)
        },
        pressed = background.pressed.caLayer().also {
            layer.addSublayer(it)
        }
    )

    stateLayers.update(this)

    this.onBoundsChanged(
        context = stateLayers,
    ) { button, stateLayers ->
        val (width: CGFloat, height: CGFloat) = button.layer.bounds.useContents {
            this.size.width to this.size.height
        }

        CATransaction.begin()
        CATransaction.setDisableActions(true)

        stateLayers.normal.frame = CGRectMake(0.0, 0.0, width, height)
        stateLayers.disabled.frame = CGRectMake(0.0, 0.0, width, height)
        stateLayers.pressed.frame = CGRectMake(0.0, 0.0, width, height)

        stateLayers.update(button)

        CATransaction.commit()
    }
}

private data class StateLayers(
    val normal: CALayer,
    val disabled: CALayer,
    val pressed: CALayer
) {
    fun update(control: UIControl) {
        if (!control.isEnabled()) {
            disabled.opacity = 1.0f
            normal.opacity = 0f
            pressed.opacity = 0f
            return
        }

        if (control.isHighlighted()) {
            pressed.opacity = 1.0f
            normal.opacity = 0f
        } else {
            normal.opacity = 1.0f
            pressed.opacity = 0f
        }
        disabled.opacity = 0f
    }
}

fun UIView.applyBackgroundIfNeeded(background: Background<Fill.Solid>?) {
    if (background == null) return

    background.fill?.also { backgroundColor = it.color.toUIColor() }
    background.border?.also {
        layer.borderWidth = it.width.toDouble()
        layer.borderColor = it.color.toUIColor().CGColor
    }
    background.cornerRadius?.also { layer.cornerRadius = it.toDouble() }
    background.maskedCorners?.also { cornersList ->
        layer.maskedCorners = cornersList.toCACornerMask()
    }
}

fun UIView.applyBackgroundIfNeeded(background: Background<out Fill>?) {
    if (background == null) return

    this.backgroundColor = UIColor.clearColor

    val bgLayer: CALayer = background.caLayer()
    layer.insertSublayer(bgLayer, 0U)

    this.onBoundsChanged(
        context = bgLayer,
    ) { view, bgLayer ->
        val (width: CGFloat, height: CGFloat) = view.layer.bounds.useContents {
            this.size.width to this.size.height
        }

        CATransaction.begin()
        CATransaction.setDisableActions(true)

        bgLayer.frame = CGRectMake(0.0, 0.0, width, height)

        CATransaction.commit()
    }
}
