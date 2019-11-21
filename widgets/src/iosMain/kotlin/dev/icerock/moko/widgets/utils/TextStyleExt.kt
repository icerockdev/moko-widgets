/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.style.view.FontStyle
import dev.icerock.moko.widgets.style.view.TextStyle
import platform.QuartzCore.CATextLayer
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UITextField
import platform.UIKit.systemFontSize

fun UILabel.applyTextStyle(textStyle: TextStyle) {
    val currentFontSize = font.pointSize
    val styleSize = textStyle.size?.toDouble()
    val styleStyle = textStyle.fontStyle
    if (styleStyle != null || styleSize != null) {
        val fontSize = styleSize ?: currentFontSize
        font = when (styleStyle) {
            FontStyle.BOLD -> UIFont.boldSystemFontOfSize(fontSize = fontSize)
            else -> UIFont.systemFontOfSize(fontSize = fontSize)
        }
    }
    textStyle.color?.also { textColor = it.toUIColor() }
}

fun UITextField.applyTextStyle(textStyle: TextStyle) {
    val currentFontSize = font?.pointSize ?: UIFont.systemFontSize
    val styleSize = textStyle.size?.toDouble()
    val styleStyle = textStyle.fontStyle
    if (styleStyle != null || styleSize != null) {
        val fontSize = styleSize ?: currentFontSize
        font = when (styleStyle) {
            FontStyle.BOLD -> UIFont.boldSystemFontOfSize(fontSize = fontSize)
            else -> UIFont.systemFontOfSize(fontSize = fontSize)
        }
    }
    textStyle.color?.also { textColor = it.toUIColor() }
}

fun CATextLayer.applyTextStyle(textStyle: TextStyle) {
    textStyle.size?.let {
        fontSize = it.toDouble()
    }

    textStyle.color?.let {
        foregroundColor = it.toUIColor().CGColor
    }

    textStyle.fontStyle?.let {
        // TODO implement font styles support
    }
}
