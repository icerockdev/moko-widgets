package dev.icerock.moko.widgets.style.ext

import android.view.Gravity
import dev.icerock.moko.widgets.style.view.TextAlignment

fun TextAlignment.getGravity() = when(this) {
    TextAlignment.LEFT -> Gravity.START
    TextAlignment.RIGHT -> Gravity.END
    TextAlignment.CENTER -> Gravity.CENTER
}
