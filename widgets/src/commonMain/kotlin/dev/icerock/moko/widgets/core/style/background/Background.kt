/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.background

data class Background<F : Fill>(
    val fill: F? = null,
    val border: Border? = null,
    val cornerRadius: Float? = null
)
