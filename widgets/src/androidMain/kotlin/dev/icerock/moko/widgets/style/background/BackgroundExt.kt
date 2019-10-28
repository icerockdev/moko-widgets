package com.icerockdev.mpp.widgets.style.background

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

fun Background.buildBackground(context: Context): StateListDrawable =
    StateListDrawable().also { selector ->
        selector.addState(
            intArrayOf(-android.R.attr.state_enabled),
            makeGradient(colorsDisabled, orientation, shape, context)
        )
        selector.addState(
            intArrayOf(),
            makeGradient(colors, orientation, shape, context)
        )
    }

private fun GradientDrawable.applyShape(context: Context, newShape: Shape) {
    when (newShape.type) {
        ShapeType.OVAL -> {
            shape = GradientDrawable.OVAL
        }
        ShapeType.RECTANGLE -> {
            shape = GradientDrawable.RECTANGLE
            applyCorners(context, newShape.corners)
        }
    }
}

private fun makeGradient(
    colors: List<Int>,
    orientation: Orientation,
    shape: Shape,
    context: Context
): GradientDrawable {
    return GradientDrawable().also {
        it.colors = colors.toIntArray()
        it.orientation = orientation.toPlatformOrientation()
        it.applyShape(context, shape)
    }
}

private fun GradientDrawable.applyCorners(context: Context, corners: Corners) {
    val scale = context.resources.displayMetrics.density

    cornerRadii = floatArrayOf(
        corners.topLeft * scale,
        corners.topLeft * scale,
        corners.topRight * scale,
        corners.topRight * scale,
        corners.bottomRight * scale,
        corners.bottomRight * scale,
        corners.bottomLeft * scale,
        corners.bottomLeft * scale
    )
}

private fun Orientation.toPlatformOrientation(): GradientDrawable.Orientation = when (this) {
    Orientation.TOP_BOTTOM -> GradientDrawable.Orientation.TOP_BOTTOM
    Orientation.TR_BL -> GradientDrawable.Orientation.TR_BL
    Orientation.RIGHT_LEFT -> GradientDrawable.Orientation.RIGHT_LEFT
    Orientation.BR_TL -> GradientDrawable.Orientation.BR_TL
    Orientation.BOTTOM_TOP -> GradientDrawable.Orientation.BOTTOM_TOP
    Orientation.BL_TR -> GradientDrawable.Orientation.BL_TR
    Orientation.LEFT_RIGHT -> GradientDrawable.Orientation.LEFT_RIGHT
    Orientation.TL_BR -> GradientDrawable.Orientation.TL_BR
}
