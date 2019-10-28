package dev.icerock.moko.widgets.style.ext

import android.graphics.drawable.GradientDrawable
import dev.icerock.moko.widgets.style.background.Orientation

internal fun Orientation.toPlatformOrientation(): GradientDrawable.Orientation = when (this) {
    Orientation.TOP_BOTTOM -> GradientDrawable.Orientation.TOP_BOTTOM
    Orientation.TR_BL -> GradientDrawable.Orientation.TR_BL
    Orientation.RIGHT_LEFT -> GradientDrawable.Orientation.RIGHT_LEFT
    Orientation.BR_TL -> GradientDrawable.Orientation.BR_TL
    Orientation.BOTTOM_TOP -> GradientDrawable.Orientation.BOTTOM_TOP
    Orientation.BL_TR -> GradientDrawable.Orientation.BL_TR
    Orientation.LEFT_RIGHT -> GradientDrawable.Orientation.LEFT_RIGHT
    Orientation.TL_BR -> GradientDrawable.Orientation.TL_BR
}
