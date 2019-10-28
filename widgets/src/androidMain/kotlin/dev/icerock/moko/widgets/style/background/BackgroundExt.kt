package dev.icerock.moko.widgets.style.background

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import dev.icerock.moko.widgets.style.ext.toPlatformOrientation

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
