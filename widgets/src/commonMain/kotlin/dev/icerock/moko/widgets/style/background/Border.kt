/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.background

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.style.view.Colors

data class Border(
    val color: Color = Colors.black,
    val width: Float = 1f
)
