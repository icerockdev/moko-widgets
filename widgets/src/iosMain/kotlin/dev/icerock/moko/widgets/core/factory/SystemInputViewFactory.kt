/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyInputTypeIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.IOSFieldBorderStyle
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.TextVerticalAlignment
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.DefaultFormatterUITextFieldDelegate
import dev.icerock.moko.widgets.core.utils.DefaultTextFormatter
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.utils.toIosPattern
import dev.icerock.moko.widgets.core.widget.InputWidget
import dev.icerock.moko.widgets.core.objc.setAssociatedObject
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSMutableAttributedString
import platform.Foundation.create
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
import platform.UIKit.UITextFieldDelegateProtocol
import platform.UIKit.clipsToBounds
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
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

        var valueFormatter: DefaultTextFormatter? = null

        widget.inputType?.mask?.let { mask ->
            valueFormatter = DefaultTextFormatter(
                mask.toIosPattern(),
                patternSymbol = '#'
            )
        }

        val delegate = DefaultFormatterUITextFieldDelegate(
            inputFormatter = valueFormatter
        )
        textField.delegate = delegate
        setAssociatedObject(textField, delegate)

        textField.setEventHandler(UIControlEventEditingChanged) {
            val currentValue = widget.field.data.value
            val newValue = textField.text

            if (currentValue != newValue) {
                widget.field.data.value = newValue.orEmpty()
            }
        }

        widget.enabled?.bind { textField.enabled = it }
        widget.label.bind { textField.placeholder = it.localized() }
        widget.field.data.bind { textField.text = valueFormatter?.format(it) ?: it }

        return ViewBundle(
            view = textField,
            size = size,
            margins = margins
        )
    }
}