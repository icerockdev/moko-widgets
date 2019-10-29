/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.view

data class TextStyle(
    val size: Int = 15,
    val color: Int = 0xFF000000.toInt(),
    val fontStyle: FontStyle = FontStyle.MEDIUM
)
