/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.IOSFieldBorderStyle
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.TextVerticalAlignment
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.widget.InputWidget
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
import platform.UIKit.UITextBorderStyle
import platform.UIKit.UITextField
import platform.UIKit.clipsToBounds
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual open class SystemInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    override val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
    private val labelTextColor: Color?,
    private val textHorizontalAlignment: TextHorizontalAlignment?,
    private val textVerticalAlignment: TextVerticalAlignment?,
    private val iosFieldBorderStyle: IOSFieldBorderStyle?
) : BaseInputViewFactory<UITextField>(), ViewFactory<InputWidget<out WidgetSize>> {

    override fun createTextField(widget: InputWidget<out WidgetSize>): Pair<UITextField, UITextField> {
        val textField = UITextField(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(this@SystemInputViewFactory.background)
            applyTextStyleIfNeeded(textStyle)
            widget.inputType?.applyTo(this)

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
        // no hierarchy
        return textField to textField
    }
}
