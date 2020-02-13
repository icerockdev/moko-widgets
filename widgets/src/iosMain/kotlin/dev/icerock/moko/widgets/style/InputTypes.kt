/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style

import dev.icerock.moko.widgets.style.input.InputType
import platform.UIKit.UIKeyboardType
import platform.UIKit.UITextField
import platform.UIKit.UIKeyboardTypeEmailAddress
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeDecimalPad
import platform.UIKit.UIKeyboardTypePhonePad
import platform.UIKit.UIKeyboardTypeNumberPad


fun UITextField.applyInputTypeIfNeeded(
    type: InputType?
) {
    if (type == null) return
    this.keyboardType = type.toPlatformInputType()
    this.secureTextEntry = type == InputType.PASSWORD
}

private fun InputType.toPlatformInputType(): UIKeyboardType {
    return when (this) {
        InputType.EMAIL -> UIKeyboardTypeEmailAddress
        InputType.PLAIN_TEXT -> UIKeyboardTypeDefault
        InputType.PASSWORD -> UIKeyboardTypeDefault
        InputType.DATE -> UIKeyboardTypeDecimalPad
        InputType.PHONE -> UIKeyboardTypePhonePad
        InputType.DIGITS -> UIKeyboardTypeNumberPad
    }
}