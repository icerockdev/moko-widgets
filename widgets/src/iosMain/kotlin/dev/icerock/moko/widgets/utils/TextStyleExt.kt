/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.style.state.PressableState
import dev.icerock.moko.widgets.style.view.FontStyle
import dev.icerock.moko.widgets.style.view.TextStyle
import platform.CoreText.CTFontCreateCopyWithSymbolicTraits
import platform.CoreText.CTFontCreateUIFontForLanguage
import platform.CoreText.kCTFontBoldTrait
import platform.CoreText.kCTFontUIFontSystem
import platform.QuartzCore.CATextLayer
import platform.UIKit.UIButton
import platform.UIKit.UIControlStateDisabled
import platform.UIKit.UIControlStateHighlighted
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIFont
import platform.UIKit.UIFontWeightMedium
import platform.UIKit.UILabel
import platform.UIKit.UITextField
import platform.UIKit.systemFontSize

fun TextStyle<*>.toUIFont(defaultFontSize: Double = 17.0): UIFont? { // If this is ok, can be applied to other methods
    val styleSize = size?.toDouble()
    val styleStyle = fontStyle
    if (styleStyle != null || styleSize != null) {
        val fontSize = styleSize ?: defaultFontSize
        return when (styleStyle) {
            FontStyle.BOLD -> UIFont.boldSystemFontOfSize(fontSize = fontSize)
            FontStyle.MEDIUM -> UIFont.systemFontOfSize(fontSize = fontSize, weight = UIFontWeightMedium)
            else -> UIFont.systemFontOfSize(fontSize = fontSize)
        }
    }
    return null
}

fun UIButton.applyTextStyleIfNeeded(textStyle: TextStyle<PressableState<Color>>?) {
    if (textStyle == null) return

    val currentFontSize = titleLabel?.font?.pointSize ?: 14.0
    val styleSize = textStyle.size?.toDouble()
    val styleStyle = textStyle.fontStyle
    if (styleStyle != null || styleSize != null) {
        val fontSize = styleSize ?: currentFontSize
        titleLabel?.font = when (styleStyle) {
            FontStyle.BOLD -> UIFont.boldSystemFontOfSize(fontSize = fontSize)
            else -> UIFont.systemFontOfSize(fontSize = fontSize)
        }
    }

    textStyle.color?.also {
        setTitleColor(color = it.normal.toUIColor(), forState = UIControlStateNormal)
        setTitleColor(color = it.pressed.toUIColor(), forState = UIControlStateHighlighted)
        setTitleColor(color = it.disabled.toUIColor(), forState = UIControlStateDisabled)
    }
}

fun UILabel.applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
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

fun UITextField.applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
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

fun CATextLayer.applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
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
