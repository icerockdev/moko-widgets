/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.style.state.PressableState
import dev.icerock.moko.widgets.core.style.view.FontStyle
import dev.icerock.moko.widgets.core.style.view.TextStyle
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
import platform.UIKit.UITextView

@Suppress("MagicNumber")
fun TextStyle<*>.toUIFont(defaultFontSize: Double = 17.0): UIFont? {
    val styleSize = size?.toDouble()
    val fontStyle = fontStyle
    if (fontStyle != null || styleSize != null) {
        val fontSize = styleSize ?: defaultFontSize
        return when (fontStyle) {
            FontStyle.BOLD -> UIFont.boldSystemFontOfSize(fontSize = fontSize)
            FontStyle.MEDIUM -> UIFont.systemFontOfSize(
                fontSize = fontSize,
                weight = UIFontWeightMedium
            )
            FontStyle.ITALIC -> UIFont.italicSystemFontOfSize(fontSize = fontSize)
            else -> UIFont.systemFontOfSize(fontSize = fontSize)
        }
    }
    return null
}

@Suppress("MagicNumber")
fun UIButton.applyTextStyleIfNeeded(textStyle: TextStyle<PressableState<Color>>?) {
    if (textStyle == null) return

    val textFont = textStyle.toUIFont(14.0)
    if (textFont != null) titleLabel?.font = textFont

    textStyle.color?.also {
        setTitleColor(color = it.normal.toUIColor(), forState = UIControlStateNormal)
        setTitleColor(color = it.pressed.toUIColor(), forState = UIControlStateHighlighted)
        setTitleColor(color = it.disabled.toUIColor(), forState = UIControlStateDisabled)
    }
}

fun UILabel.applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
    if (textStyle == null) return

    font = textStyle.toUIFont() ?: font
    textStyle.color?.also { textColor = it.toUIColor() }
}

fun UITextField.applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
    if (textStyle == null) return

    font = textStyle.toUIFont() ?: font
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

fun UITextView.applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
    if (textStyle == null) return

    font = textStyle.toUIFont() ?: font
    textStyle.color?.also { textColor = it.toUIColor() }
}
