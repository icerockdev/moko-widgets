/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.input

import dev.icerock.moko.widgets.core.objc.setAssociatedObject
import dev.icerock.moko.widgets.core.utils.DefaultFormatterUITextFieldDelegate
import dev.icerock.moko.widgets.core.utils.DefaultTextFormatter
import dev.icerock.moko.widgets.core.utils.toIosPattern
import platform.UIKit.UIKeyboardTypeDecimalPad
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeEmailAddress
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UIKeyboardTypePhonePad
import platform.UIKit.UITextField
import platform.UIKit.UITextView

actual interface InputType {

    fun applyTo(textField: UITextField)

    fun applyTo(textView: UITextView)

    actual companion object
}

actual fun InputType.Companion.plain(mask: String?): InputType {
    return object : InputType {

        override fun applyTo(textField: UITextField) {
            textField.keyboardType = UIKeyboardTypeDefault
            textField.secureTextEntry = false
            applyFormatter(mask, textField)
        }

        override fun applyTo(textView: UITextView) {
            textView.keyboardType = UIKeyboardTypeDefault
            textView.secureTextEntry = false
        }
    }
}

actual fun InputType.Companion.email(mask: String?): InputType {
    return object : InputType {

        override fun applyTo(textField: UITextField) {
            textField.keyboardType = UIKeyboardTypeEmailAddress
            textField.secureTextEntry = false
            applyFormatter(mask, textField)
        }

        override fun applyTo(textView: UITextView) {
            textView.keyboardType = UIKeyboardTypeEmailAddress
            textView.secureTextEntry = false
        }
    }
}

actual fun InputType.Companion.phone(mask: String?): InputType {
    return object : InputType {

        override fun applyTo(textField: UITextField) {
            textField.keyboardType = UIKeyboardTypePhonePad
            textField.secureTextEntry = false
            applyFormatter(mask, textField)
        }

        override fun applyTo(textView: UITextView) {
            textView.keyboardType = UIKeyboardTypePhonePad
            textView.secureTextEntry = false
        }
    }
}

actual fun InputType.Companion.password(mask: String?): InputType {
    return object : InputType {

        override fun applyTo(textField: UITextField) {
            textField.keyboardType = UIKeyboardTypeDefault
            textField.secureTextEntry = true
            applyFormatter(mask, textField)
        }

        override fun applyTo(textView: UITextView) {
            textView.keyboardType = UIKeyboardTypeDefault
            textView.secureTextEntry = true
        }
    }
}

actual fun InputType.Companion.date(mask: String?): InputType {
    return object : InputType {

        override fun applyTo(textField: UITextField) {
            textField.keyboardType = UIKeyboardTypeDecimalPad
            textField.secureTextEntry = false
            applyFormatter(mask, textField)
        }

        override fun applyTo(textView: UITextView) {
            textView.keyboardType = UIKeyboardTypeDecimalPad
            textView.secureTextEntry = false
        }
    }
}

actual fun InputType.Companion.digits(mask: String?): InputType {
    return object : InputType {

        override fun applyTo(textField: UITextField) {
            textField.keyboardType = UIKeyboardTypeNumberPad
            textField.secureTextEntry = false
            applyFormatter(mask, textField)
        }

        override fun applyTo(textView: UITextView) {
            textView.keyboardType = UIKeyboardTypeNumberPad
            textView.secureTextEntry = false
        }
    }
}

private fun applyFormatter(mask: String?, textField: UITextField) {
    val delegate = DefaultFormatterUITextFieldDelegate(inputFormatter = mask?.let { createDefaultTextFormatter(it) })
    textField.delegate = delegate
    setAssociatedObject(textField, delegate)
}

internal fun createDefaultTextFormatter(mask: String) = DefaultTextFormatter(
    mask.toIosPattern(),
    patternSymbol = '#'
)
