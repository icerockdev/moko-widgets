/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UITextBorderStyle
import platform.UIKit.UITextField
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var inputWidgetViewFactory: VFC<InputWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val textField = UITextField(frame = CGRectZero.readValue())
    textField.translatesAutoresizingMaskIntoConstraints = false
    textField.borderStyle = UITextBorderStyle.UITextBorderStyleRoundedRect

    widget.field.data.bind { textField.text = it }

    textField.setEventHandler(UIControlEventEditingChanged) {
        val text = textField.text
        val currentValue = widget.field.data.value

        if (currentValue != text) {
            widget.field.data.value = text.orEmpty()
        }
    }

    textField
}
