package dev.icerock.moko.widgets.style

import dev.icerock.moko.widgets.style.input.InputType
import platform.Foundation.*
import platform.UIKit.*

fun UITextField.applyInputType(
    type: InputType
) {
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
