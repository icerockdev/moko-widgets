/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.ext

import android.view.Gravity
import dev.icerock.moko.widgets.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.style.view.TextVerticalAlignment

fun TextHorizontalAlignment.getGravity() = when(this) {
    TextHorizontalAlignment.LEFT -> Gravity.START
    TextHorizontalAlignment.RIGHT -> Gravity.END
    TextHorizontalAlignment.CENTER -> Gravity.CENTER_HORIZONTAL
}

fun TextVerticalAlignment.getGravity() = when(this) {
    TextVerticalAlignment.TOP -> Gravity.TOP
    TextVerticalAlignment.MIDDLE -> Gravity.CENTER_VERTICAL
    TextVerticalAlignment.BOTTOM -> Gravity.BOTTOM
}

fun getGravityForTextAlignment(
    textHorizontalAlignment: TextHorizontalAlignment?,
    textVerticalAlignment: TextVerticalAlignment?
): Int {
    return if(textHorizontalAlignment != null && textVerticalAlignment != null) {
        textHorizontalAlignment.getGravity() or textVerticalAlignment.getGravity()
    } else if(textHorizontalAlignment != null) {
        textHorizontalAlignment.getGravity()
    } else if(textVerticalAlignment != null) {
        textVerticalAlignment.getGravity()
    } else {
        throw IllegalArgumentException("At least one argument must be nonnull.")
    }
}
