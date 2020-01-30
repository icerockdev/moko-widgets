/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.ext

import android.view.Gravity
import dev.icerock.moko.widgets.style.view.TextAlignment

fun TextAlignment.getGravity() = when(this) {
    TextAlignment.LEFT -> Gravity.START
    TextAlignment.RIGHT -> Gravity.END
    TextAlignment.CENTER -> Gravity.CENTER
}
