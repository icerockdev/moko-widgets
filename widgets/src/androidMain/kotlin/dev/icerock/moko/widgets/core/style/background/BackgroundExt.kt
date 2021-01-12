/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.background

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import dev.icerock.moko.widgets.core.style.ext.toPlatformOrientation
import dev.icerock.moko.widgets.core.style.state.PressableState
import kotlin.math.ceil

fun PressableState<Background<out Fill>>.buildBackground(context: Context) = StateListDrawable().also { selector ->
    selector.addState(
        intArrayOf(-android.R.attr.state_enabled),
        disabled.buildBackground(context)
    )
    val pressedBg = pressed.buildBackground(context).let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RippleDrawable(
                ColorStateList.valueOf(Color.GRAY),
                it,
                null
            )
        } else {
            it
        }
    }
    selector.addState(
        intArrayOf(android.R.attr.state_pressed),
        pressedBg
    )
    selector.addState(
        intArrayOf(),
        normal.buildBackground(context)
    )
}

fun Background<out Fill>.buildBackground(context: Context): Drawable {
    val gradientDrawable = GradientDrawable()
    val scale = context.resources.displayMetrics.density

    when (fill) {
        is Fill.Solid -> gradientDrawable.setColor(fill.color.argb.toInt())
        is Fill.Gradient -> {
            gradientDrawable.colors = fill.colors.map { it.argb.toInt() }.toIntArray()
            gradientDrawable.orientation = fill.direction.toPlatformOrientation()
        }
    }

    if (cornerRadius != null) {
        var topLeftRadius = 0f
        var topRightRadius = 0f
        var bottomLeftRadius = 0f
        var bottomRightRadius = 0f

        (maskedCorners ?: Corner.values().toList()).onEach { corner ->
            when (corner) {
                Corner.TOP_LEFT -> topLeftRadius = cornerRadius * scale
                Corner.TOP_RIGHT -> topRightRadius = cornerRadius * scale
                Corner.BOTTOM_LEFT -> bottomLeftRadius = cornerRadius * scale
                Corner.BOTTOM_RIGHT -> bottomRightRadius = cornerRadius * scale
            }
        }

        gradientDrawable.cornerRadii = floatArrayOf(
            topLeftRadius, topLeftRadius,
            topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius
        )
    }

    if (border != null) {
        gradientDrawable.setStroke(ceil(border.width * scale).toInt(), border.color.argb.toInt())
    }

    return gradientDrawable
}
