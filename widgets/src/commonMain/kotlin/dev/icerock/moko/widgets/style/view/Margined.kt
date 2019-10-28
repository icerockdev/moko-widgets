package com.icerockdev.mpp.widgets.style.view

/**
 * Defines a widget that has margins.
 *
 * @property margins margins for four sides
 */
interface Margined {
    val margins: MarginValues
}

/**
 * Margin values
 */
data class MarginValues(
    val start: Float = 0.0F,
    val top: Float = 0.0F,
    val end: Float = 0.0F,
    val bottom: Float = 0.0F
) {
    constructor(marginAll: Float) : this(marginAll, marginAll, marginAll, marginAll)
}