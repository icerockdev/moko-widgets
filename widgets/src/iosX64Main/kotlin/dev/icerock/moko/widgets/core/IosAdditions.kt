/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import kotlinx.cinterop.CValue
import platform.Foundation.NSRange
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol

actual fun UITextFieldDelegateProtocol.shouldChangeCharacters(
    textField: UITextField,
    range: CValue<NSRange>,
    text: String
): Boolean {
    return textField(
        textField = textField,
        shouldChangeCharactersInRange = range,
        replacementString = text
    )
}

actual var Any.associatedObject: Any
    get() {

    }
    set(value) {

    }
