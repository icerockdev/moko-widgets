/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.view

import dev.icerock.moko.graphics.Color

object Colors {
    val white get() = Color(0xFF, 0xFF, 0xFF, 0xFF)
    val black get() = Color(0x00, 0x00, 0x00, 0xFF)
}

fun ColorF(red: Double, green: Double, blue: Double, alpha: Double) = Color(
    red = (0xFF * red).toInt(),
    green = (0xFF * green).toInt(),
    blue = (0xFF * blue).toInt(),
    alpha = (0xFF * alpha).toInt()
)

fun rgba(red: Int, green: Int, blue: Int, alpha: Double) = Color(
    red = red,
    green = green,
    blue = blue,
    alpha = (0xFF * alpha).toInt()
)
