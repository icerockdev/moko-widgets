/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.widgets.core.objc.cgColors
import dev.icerock.moko.widgets.core.objc.getAssociatedObject
import dev.icerock.moko.widgets.core.objc.setAssociatedObject
import kotlinx.cinterop.CValue
import platform.Foundation.NSRange
import platform.UIKit.UIColor
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

actual var Any.associatedObject: Any?
    get() {
        return getAssociatedObject(this)
    }
    set(value) {
        setAssociatedObject(this, value)
    }

actual fun List<UIColor>.toCGColor(): List<*>? = cgColors(this)
