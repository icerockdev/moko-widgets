/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.view

/**
 * Desired widget size defined as specs for width and height.
 *
 * @property width widget's desirable width, could be either one of SizeSpecs or an exact value in dp
 * @property height widget's desirable height, could be either one of SizeSpecs or an exact value in dp
 */
sealed class WidgetSize {
    data class Const<W : SizeSpec, H : SizeSpec>(
        val width: W,
        val height: H
    ) : WidgetSize()

    data class AspectByWidth<W : SizeSpec>(
        val width: W,
        val aspectRatio: Float = 1.0f
    ) : WidgetSize()

    data class AspectByHeight<H : SizeSpec>(
        val height: H,
        val aspectRatio: Float = 1.0f
    ) : WidgetSize()
}