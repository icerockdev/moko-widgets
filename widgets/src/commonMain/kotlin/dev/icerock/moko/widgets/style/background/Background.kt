package dev.icerock.moko.widgets.style.background

data class Background(
    val colors: List<Int> = listOf(0xFFFFFFFF.toInt(), 0xFFFFFFFF.toInt()),
    val colorsDisabled: List<Int> = colors,
    val orientation: Orientation = Orientation.LEFT_RIGHT,
    val shape: Shape = Shape()
) {
    constructor(
        color: Int,
        colorDisabled: Int = color,
        orientation: Orientation = Orientation.LEFT_RIGHT,
        shape: Shape = Shape()
    ) : this(listOf(color, color), listOf(colorDisabled, colorDisabled), orientation, shape)
}

/**
 * Values are taken from Android's GradientDrawable
 */
enum class Orientation {
    /** draw the gradient from the top to the bottom  */
    TOP_BOTTOM,
    /** draw the gradient from the top-right to the bottom-left  */
    TR_BL,
    /** draw the gradient from the right to the left  */
    RIGHT_LEFT,
    /** draw the gradient from the bottom-right to the top-left  */
    BR_TL,
    /** draw the gradient from the bottom to the top  */
    BOTTOM_TOP,
    /** draw the gradient from the bottom-left to the top-right  */
    BL_TR,
    /** draw the gradient from the left to the right  */
    LEFT_RIGHT,
    /** draw the gradient from the top-left to the bottom-right  */
    TL_BR
}