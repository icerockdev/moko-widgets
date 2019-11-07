/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.background

sealed class Shape {
    data class Rectangle(val cornerRadius: Float? = null) : Shape()
    object Oval : Shape()
}
