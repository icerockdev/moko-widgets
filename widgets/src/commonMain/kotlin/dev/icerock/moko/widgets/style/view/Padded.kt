package com.icerockdev.mpp.widgets.style.view

/**
 * Defines a widget that has inner paddings
 */
interface Padded {
    val paddings: PaddingValues
}

data class PaddingValues(
    val start: Float = 0.0F,
    val top: Float = 0.0F,
    val end: Float = 0.0F,
    val bottom: Float = 0.0F
) {
    constructor(padding: Float = 0.0F) : this(
        start = padding,
        top = padding,
        end = padding,
        bottom = padding
    )
}
