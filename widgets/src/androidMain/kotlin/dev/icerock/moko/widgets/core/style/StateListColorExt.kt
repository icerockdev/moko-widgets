/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style

import android.content.res.ColorStateList
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.style.state.PressableState

fun PressableState<Color>.toStateList() = ColorStateList(
    arrayOf(
        intArrayOf(-android.R.attr.state_enabled),
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf()
    ),
    intArrayOf(
        disabled.argb.toInt(),
        pressed.argb.toInt(),
        normal.argb.toInt()
    )
)
