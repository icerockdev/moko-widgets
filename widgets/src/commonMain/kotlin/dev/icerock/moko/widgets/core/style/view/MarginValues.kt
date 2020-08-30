/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.view

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

    operator fun plus(other: MarginValues) = MarginValues(
        top = top + other.top,
        start = start + other.start,
        end = end + other.end,
        bottom = bottom + other.bottom
    )
}
