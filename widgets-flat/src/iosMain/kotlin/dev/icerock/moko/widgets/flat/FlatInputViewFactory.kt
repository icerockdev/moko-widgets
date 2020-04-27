/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.flat

import cocoapods.mokoWidgetsFlat.FlatInputField
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.widget.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.input.InputType
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIFont
import platform.UIKit.UIKeyboardTypeEmailAddress
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UIKeyboardTypePhonePad
import platform.UIKit.backgroundColor

actual class FlatInputViewFactory actual constructor(
    private val textStyle: TextStyle<Color>?,
    private val backgroundColor: Color?,
    private val margins: MarginValues?
) : ViewFactory<InputWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val flatInputField = FlatInputField(frame = CGRectZero.readValue())
        val textField = flatInputField.textField()!!

        textStyle?.color?.also {
            textField.textColor = it.toUIColor()
        }
        textStyle?.size?.also {
            textField.font = UIFont.systemFontOfSize(it.toDouble())
        }

        widget.field.data.bind {
            if (textField.text == it) return@bind
            textField.text = it
        }
        textField.setEventHandler(UIControlEventEditingChanged) {
            textField.text?.also { widget.field.data.value = it }
        }

        widget.label.bind {
            textField.placeholder = it.localized()
        }

        widget.inputType?.let { inputType ->
            inputType.applyTo(textField)
            flatInputField.setFormat(inputType.mask)
        }

        flatInputField.backgroundColor = backgroundColor?.toUIColor()

        return ViewBundle(
            view = flatInputField,
            size = size,
            margins = margins
        )
    }
}
