/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyInputTypeIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.*
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.setEventHandler
import dev.icerock.moko.widgets.utils.DefaultTextFormatter
import dev.icerock.moko.widgets.utils.toIosPattern
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.create
import platform.Foundation.NSRange
import platform.Foundation.NSString
import platform.Foundation.stringByReplacingCharactersInRange
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.UIControlContentVerticalAlignmentBottom
import platform.UIKit.UIControlContentVerticalAlignmentCenter
import platform.UIKit.UIControlContentVerticalAlignmentTop
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UITextBorderStyle
import platform.UIKit.UITextField
import platform.UIKit.clipsToBounds
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.addSubview
import platform.UIKit.UIView
import platform.UIKit.UITextFieldDelegateProtocol

actual class SystemInputViewFactory actual constructor(
    private val background: Background?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle?,
    private val labelTextColor: Color?,
    private val textHorizontalAlignment: TextHorizontalAlignment?,
    private val textVerticalAlignment: TextVerticalAlignment?,
    private val iosFieldBorderStyle: IOSFieldBorderStyle?
) : ViewFactory<InputWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        val backgroundConfig = background

        val textField = UITextField(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(backgroundConfig)
            applyTextStyleIfNeeded(textStyle)
            applyInputTypeIfNeeded(widget.inputType)

            clipsToBounds = true

            when (textHorizontalAlignment) {
                TextHorizontalAlignment.LEFT -> textAlignment = NSTextAlignmentLeft
                TextHorizontalAlignment.CENTER -> textAlignment = NSTextAlignmentCenter
                TextHorizontalAlignment.RIGHT -> textAlignment = NSTextAlignmentRight
                null -> {
                }
            }

            when (textVerticalAlignment) {
                TextVerticalAlignment.TOP -> contentVerticalAlignment =
                    UIControlContentVerticalAlignmentTop
                TextVerticalAlignment.MIDDLE -> contentVerticalAlignment =
                    UIControlContentVerticalAlignmentCenter
                TextVerticalAlignment.BOTTOM -> contentVerticalAlignment =
                    UIControlContentVerticalAlignmentBottom
                null -> {
                }
            }

            borderStyle = when (iosFieldBorderStyle) {
                IOSFieldBorderStyle.NONE -> UITextBorderStyle.UITextBorderStyleNone
                IOSFieldBorderStyle.LINE -> UITextBorderStyle.UITextBorderStyleLine
                IOSFieldBorderStyle.BEZEL -> UITextBorderStyle.UITextBorderStyleBezel
                IOSFieldBorderStyle.ROUNDED, null -> UITextBorderStyle.UITextBorderStyleRoundedRect
            }

            if (labelTextColor != null) {
                val attrString = NSMutableAttributedString.create(
                    string = widget.label.value.localized(),
                    attributes = mapOf<Any?, Any?>(
                        NSForegroundColorAttributeName to labelTextColor.toUIColor()
                    )
                )
                setAttributedPlaceholder(
                    attributedPlaceholder = attrString
                )
            }
        }

        if (widget.inputType?.mask != null) {
            val delegate = SystemInputViewDelegate(
                inputFormatter = DefaultTextFormatter(
                    widget.inputType.mask.toIosPattern(),
                    patternSymbol = '#'
                )
            ) {
                val currentValue = widget.field.data.value
                val newValue = textField.text

                if (currentValue != newValue) {
                    widget.field.data.value = newValue.orEmpty()
                }
            }
            textField.delegate = delegate
            textField.addSubview(delegate) // to have strong reference to delegate (for prevent deiniting)

        } else {
            textField.setEventHandler(UIControlEventEditingChanged) {
                val currentValue = widget.field.data.value
                val newValue = textField.text

                if (currentValue != newValue) {
                    widget.field.data.value = newValue.orEmpty()
                }
            }
        }

        widget.enabled?.bind { textField.enabled = it }
        widget.label.bind { textField.placeholder = it.localized() }
        widget.field.data.bind { textField.text = it }

        return ViewBundle(
            view = textField,
            size = size,
            margins = margins
        )
    }
}

class SystemInputViewDelegate(
    private val inputFormatter: DefaultTextFormatter,
    private val textDidChanged: () -> Unit
) : UIView(frame = CGRectZero.readValue()), UITextFieldDelegateProtocol {

    override fun textField(
        textField: UITextField,
        shouldChangeCharactersInRange: CValue<NSRange>,
        replacementString: String
    ): Boolean {
        val nsString = NSString.create(string = textField.text ?: "")
        val newText = nsString.stringByReplacingCharactersInRange(
            range = shouldChangeCharactersInRange,
            withString = replacementString
        )
        val unformattedText = inputFormatter.unformat(newText)

        textField.text = inputFormatter.format(unformattedText)
        textDidChanged()
        return false
    }
}