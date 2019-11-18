/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIAccessibilityIdentificationProtocol
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UITextBorderStyle
import platform.UIKit.UITextField
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.darwin.NSObject

actual var inputWidgetViewFactory: VFC<InputWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val textField = UITextFieldIdentifier().apply {
        translatesAutoresizingMaskIntoConstraints = false
        borderStyle = UITextBorderStyle.UITextBorderStyleRoundedRect
        accessibilityIdentifier = widget.identifier()
        applyBackground(style.background)
    }

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

class UITextFieldIdentifier : UITextField(frame = CGRectZero.readValue()),
    UIAccessibilityIdentificationProtocol {
    private var _accessibilityIdentifier: String? = null

    override fun accessibilityIdentifier(): String? {
        return _accessibilityIdentifier
    }

    override fun setAccessibilityIdentifier(accessibilityIdentifier: String?) {
        _accessibilityIdentifier = accessibilityIdentifier
    }
}
