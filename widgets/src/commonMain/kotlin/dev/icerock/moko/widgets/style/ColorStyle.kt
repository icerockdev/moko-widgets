package dev.icerock.moko.widgets

import com.icerockdev.mpp.widgets.style.background.Orientation

class ColorStyle(
        val colors: List<Int>,
        val colorsDisabled: List<Int> = colors,
        val orientation: Orientation = Orientation.LEFT_RIGHT
) {
    constructor(
            color: Int,
            colorDisabled: Int = color,
            orientation: Orientation = Orientation.LEFT_RIGHT
    ) : this(listOf(color), listOf(colorDisabled), orientation)
}
