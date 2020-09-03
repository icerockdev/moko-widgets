/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.input

import platform.UIKit.UITextField
import platform.UIKit.UITextView

actual interface InputType {

    fun applyTo(textField: UITextField)

    fun applyTo(textView: UITextView)

    actual companion object
}
