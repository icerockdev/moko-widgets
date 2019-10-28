package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.style.background.Direction

class ColorStyle(
        val colors: List<Int>,
        val colorsDisabled: List<Int> = colors,
        val direction: Direction = Direction.LEFT_RIGHT
) {
    constructor(
        color: Int,
        colorDisabled: Int = color,
        direction: Direction = Direction.LEFT_RIGHT
    ) : this(listOf(color), listOf(colorDisabled), direction)
}
