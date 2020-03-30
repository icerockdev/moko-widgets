/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.background

/**
 * Values are taken from Android's GradientDrawable
 */
enum class Direction {
    /** draw the gradient from the top to the bottom  */
    TOP_BOTTOM,
    /** draw the gradient from the top-right to the bottom-left  */
    TR_BL,
    /** draw the gradient from the right to the left  */
    RIGHT_LEFT,
    /** draw the gradient from the bottom-right to the top-left  */
    BR_TL,
    /** draw the gradient from the bottom to the top  */
    BOTTOM_TOP,
    /** draw the gradient from the bottom-left to the top-right  */
    BL_TR,
    /** draw the gradient from the left to the right  */
    LEFT_RIGHT,
    /** draw the gradient from the top-left to the bottom-right  */
    TL_BR
}
