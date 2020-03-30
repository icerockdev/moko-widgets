/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.background

import dev.icerock.moko.graphics.Color

sealed class Fill {
    data class Solid(val color: Color) : Fill()
    data class Gradient(
        val colors: List<Color>,
        val direction: Direction
    ) : Fill()
}
