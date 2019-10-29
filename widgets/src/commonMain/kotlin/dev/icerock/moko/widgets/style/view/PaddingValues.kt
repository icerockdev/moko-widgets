/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.view

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