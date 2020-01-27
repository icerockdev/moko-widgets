/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.flat

import cocoapods.mokoWidgetsFlat.FlatInputField
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIFont
import platform.UIKit.UIKeyboardType
import platform.UIKit.UIKeyboardTypeEmailAddress
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UIKeyboardTypePhonePad
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.backgroundColor

actual class FlatInputViewFactory actual constructor(
    private val textStyle: TextStyle?,
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
            if(textField.text == it) return@bind
            textField.text = it
        }
        // FIXME !!!
//        field.bindTextTwoWay(liveData: widget.field.data)

        widget.label.bind {
            textField.placeholder = it.localized()
        }

        when(widget.inputType) {
            InputType.EMAIL -> textField.keyboardType = UIKeyboardTypeEmailAddress
            InputType.PLAIN_TEXT -> {}
            InputType.PASSWORD -> textField.setSecureTextEntry(true)
            InputType.DATE -> {}
            InputType.PHONE -> textField.keyboardType = UIKeyboardTypePhonePad
            InputType.DIGITS -> textField.keyboardType = UIKeyboardTypeNumberPad
            null -> {}
        }

        widget.inputType?.mask?.also {
            flatInputField.setFormat(it)
        }

        flatInputField.backgroundColor = backgroundColor?.toUIColor()

        return ViewBundle(
            view = flatInputField,
            size = size,
            margins = margins
        )
    }
}
