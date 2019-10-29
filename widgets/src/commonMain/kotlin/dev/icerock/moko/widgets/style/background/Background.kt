/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.background

data class Background(
    val colors: List<Long> = listOf(0xFFFFFFFF, 0xFFFFFFFF),
    val colorsDisabled: List<Long> = colors,
    val direction: Direction = Direction.LEFT_RIGHT,
    val shape: Shape = Shape()
) {
    constructor(
        color: Long,
        colorDisabled: Long = color,
        direction: Direction = Direction.LEFT_RIGHT,
        shape: Shape = Shape()
    ) : this(listOf(color, color), listOf(colorDisabled, colorDisabled), direction, shape)
}
