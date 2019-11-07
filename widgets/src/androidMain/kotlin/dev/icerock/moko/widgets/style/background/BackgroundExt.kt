/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.background

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import dev.icerock.moko.widgets.style.ext.toPlatformOrientation
import kotlin.math.ceil

fun StateBackground.buildBackground(context: Context) = StateListDrawable().also { selector ->
    selector.addState(
        intArrayOf(-android.R.attr.state_enabled),
        disabled.buildBackground(context)
    )
    // TODO Ripple?
//    val rippleDrawable = RippleDrawable(
//        ColorStateList.valueOf(Color.GRAY),
//        it.buildBackground(ctx), null
//    )
    selector.addState(
        intArrayOf(android.R.attr.state_pressed),
        pressed.buildBackground(context)
    )
    selector.addState(
        intArrayOf(),
        normal.buildBackground(context)
    )
}

fun Background.buildBackground(context: Context): Drawable {
    val gradientDrawable = GradientDrawable()
    val scale = context.resources.displayMetrics.density

    when (fill) {
        is Fill.Solid -> gradientDrawable.setColor(fill.color.argb.toInt())
        is Fill.Gradient -> {
            gradientDrawable.colors = fill.colors.map { it.argb.toInt() }.toIntArray()
            gradientDrawable.orientation = fill.direction.toPlatformOrientation()
        }
    }

    when (shape) {
        is Shape.Rectangle -> {
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            if (shape.cornerRadius != null) {
                gradientDrawable.cornerRadius = shape.cornerRadius * scale
            }
        }
        is Shape.Oval -> {
            gradientDrawable.shape = GradientDrawable.OVAL
        }
    }

    if (border != null) {
        gradientDrawable.setStroke(ceil(border.width * scale).toInt(), border.color.argb.toInt())
    }

    return gradientDrawable
}
