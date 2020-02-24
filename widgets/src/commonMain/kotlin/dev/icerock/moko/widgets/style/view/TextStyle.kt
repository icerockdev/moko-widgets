/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.view

data class TextStyle<C>(
    val size: Int? = null,
    val color: C? = null,
    val fontStyle: FontStyle? = null
)
