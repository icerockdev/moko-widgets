/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import platform.Foundation.create
import platform.Foundation.NSRange
import platform.Foundation.NSString
import platform.Foundation.stringByReplacingCharactersInRange
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UITextField
import platform.UIKit.UIView
import platform.UIKit.UITextFieldDelegateProtocol


class DefaultTextFormatter(val textPattern: String, val patternSymbol: Char = '#') {

    fun format(unformattedText: String): String {

        var formatted = ""
        var unformattedIndex = 0
        var patternIndex = 0

        while (patternIndex < textPattern.length && unformattedIndex < unformattedText.length) {
            if (textPattern.length <= patternIndex) break
            val patternCharacter = textPattern[patternIndex]
            if (patternCharacter == patternSymbol) {
                if (unformattedIndex < unformattedText.length) {
                    formatted = formatted.plus(unformattedText[unformattedIndex])
                }
                unformattedIndex += 1
            } else {
                formatted = formatted.plus(patternCharacter)
            }
            patternIndex += 1
        }
        return formatted
    }

    fun unformat(formatted: String): String {
        var unformatted = ""
        var formattedIndex = 0

        while (formattedIndex < formatted.length) {
            if (formattedIndex < formatted.length) {
                val formattedCharacter = formatted[formattedIndex]
                if (formattedIndex >= textPattern.length) {
                    unformatted = unformatted.plus(formattedCharacter)
                } else if (formattedCharacter != textPattern[formattedIndex]) {
                    unformatted = unformatted.plus(formattedCharacter)
                }
                formattedIndex += 1
            }

        }
        return unformatted
    }
}

fun String.toIosPattern(): String {
    return this.replace("0", "#")
        .replace("[", "")
        .replace("]", "")
}

class DefaultFormatterUITextFieldDelegate(
    private val inputFormatter: DefaultTextFormatter
) : UIView(frame = CGRectZero.readValue()), UITextFieldDelegateProtocol {

    override fun textField(
        textField: UITextField,
        shouldChangeCharactersInRange: CValue<NSRange>,
        replacementString: String
    ): Boolean {
        val nsString = NSString.create(string = textField.text ?: "")
        val newText = nsString.stringByReplacingCharactersInRange(
            range = shouldChangeCharactersInRange,
            withString = replacementString
        )
        val unformattedText = inputFormatter.unformat(newText)

        textField.text = inputFormatter.format(unformattedText)
        textField.sendActionsForControlEvents(UIControlEventEditingChanged)
        return false
    }
}