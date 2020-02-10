/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.style.view.FontStyle
import dev.icerock.moko.widgets.style.view.TextStyle
import platform.CoreText.CTFontCreateCopyWithSymbolicTraits
import platform.CoreText.CTFontCreateUIFontForLanguage
import platform.CoreText.kCTFontBoldTrait
import platform.CoreText.kCTFontUIFontSystem
import platform.QuartzCore.CATextLayer
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UITextField
import platform.UIKit.systemFontSize
import platform.UIKit.UIFontWeightMedium

fun TextStyle.toUIFont(defautlFontSize: Double = 17.0): UIFont? { // If this is ok, can be applied to other methods
    val styleSize = size?.toDouble()
    val styleStyle = fontStyle
    if (styleStyle != null || styleSize != null) {
        val fontSize = styleSize ?: defautlFontSize
        return when (styleStyle) {
            FontStyle.BOLD -> UIFont.boldSystemFontOfSize(fontSize = fontSize)
            FontStyle.MEDIUM -> UIFont.systemFontOfSize(fontSize = fontSize, weight = UIFontWeightMedium)
            else -> UIFont.systemFontOfSize(fontSize = fontSize)
        }
    }
    return null
}

fun UILabel.applyTextStyleIfNeeded(textStyle: TextStyle?) {
    if (textStyle == null) return

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

fun UITextField.applyTextStyleIfNeeded(textStyle: TextStyle?) {
    if (textStyle == null) return

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

fun CATextLayer.applyTextStyleIfNeeded(textStyle: TextStyle?) {
    if (textStyle == null) return

    textStyle.size?.let {
        fontSize = it.toDouble()
    }

    textStyle.color?.let {
        foregroundColor = it.toUIColor().CGColor
    }

    textStyle.fontStyle?.let {
        val cfFont = CTFontCreateUIFontForLanguage(
            uiType = kCTFontUIFontSystem,
            size = fontSize,
            language = null
        )
        font = CTFontCreateCopyWithSymbolicTraits(
            font = cfFont,
            size = fontSize,
            matrix = null,
            symTraitValue = if (it == FontStyle.BOLD) kCTFontBoldTrait else 0U,
            symTraitMask = kCTFontBoldTrait
        )
    }
}
