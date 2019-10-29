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
class WidgetSize(
    val width: Int = SizeSpec.WRAP_CONTENT,
    val height: Int = SizeSpec.WRAP_CONTENT
)
