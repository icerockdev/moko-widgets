/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.widget.InputWidget
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.UIColor
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UITextView
import platform.UIKit.UITextViewTextDidBeginEditingNotification
import platform.UIKit.UITextViewTextDidChangeNotification
import platform.UIKit.UITextViewTextDidEndEditingNotification
import platform.UIKit.clipsToBounds
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN_NONATOMIC
import platform.objc.objc_setAssociatedObject

actual class MultilineInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
    private val labelTextColor: Color?,
    private val textHorizontalAlignment: TextHorizontalAlignment?
) : ViewFactory<InputWidget<out WidgetSize>> {

    @Suppress("LongMethod")
    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        val textView = UITextView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyBackgroundIfNeeded(background)
            applyTextStyleIfNeeded(textStyle)
            widget.inputType?.applyTo(this)

            padding?.also {
                contentInset = UIEdgeInsetsMake(
                    top = it.top.toDouble(),
                    left = it.start.toDouble(),
                    bottom = it.bottom.toDouble(),
                    right = it.end.toDouble()
                )
            }

            if (isWrapContent(size)) {
                setScrollEnabled(false)
            }

            clipsToBounds = true

            when (textHorizontalAlignment) {
                TextHorizontalAlignment.LEFT -> textAlignment = NSTextAlignmentLeft
                TextHorizontalAlignment.CENTER -> textAlignment = NSTextAlignmentCenter
                TextHorizontalAlignment.RIGHT -> textAlignment = NSTextAlignmentRight
                null -> {
                }
            }
        }

        widget.enabled?.bind { textView.editable = it }
        widget.field.data.bind { textView.text = it }

        val nc = NSNotificationCenter.defaultCenter
        val observer = TextViewObserver(
            textView = textView,
            isPlaceholderShow = widget.field.data.value.isEmpty(),
            textChangedHandler = { newValue ->
                val currentValue = widget.field.data.value

                if (currentValue != newValue) {
                    widget.field.data.value = newValue
                }
            },
            placeholderText = widget.label.value,
            placeholderColor = labelTextColor,
            textColor = textStyle?.color
        )
        nc.addObserver(
            observer = observer,
            selector = NSSelectorFromString("textViewDidBeginEditing:"),
            name = UITextViewTextDidBeginEditingNotification,
            `object` = null
        )
        nc.addObserver(
            observer = observer,
            selector = NSSelectorFromString("textViewDidEndEditing:"),
            name = UITextViewTextDidEndEditingNotification,
            `object` = null
        )
        nc.addObserver(
            observer = observer,
            selector = NSSelectorFromString("textViewDidChange:"),
            name = UITextViewTextDidChangeNotification,
            `object` = null
        )

        if (widget.label.value.localized().isNotEmpty()) {
            if (widget.field.data.value.isEmpty()) {
                textView.text = widget.label.value.localized()
                textView.textColor = labelTextColor?.toUIColor() ?: UIColor.grayColor
            }
        }

        objc_setAssociatedObject(
            `object` = textView,
            key = "textViewObserver".cstr,
            value = observer,
            policy = OBJC_ASSOCIATION_RETAIN_NONATOMIC
        )

        return ViewBundle(
            view = textView,
            size = size,
            margins = margins
        )
    }

    private fun isWrapContent(size: WidgetSize): Boolean {
        return when (size) {
            is WidgetSize.Const<*, *> -> size.height is SizeSpec.WrapContent
            is WidgetSize.AspectByHeight<*> -> size.height is SizeSpec.WrapContent
            else -> false
        }
    }

    private class TextViewObserver(
        private val textView: UITextView,
        private var isPlaceholderShow: Boolean,
        private val placeholderColor: Color?,
        private val textColor: Color?,
        private val placeholderText: StringDesc?,
        private val textChangedHandler: (String) -> Unit
    ) : NSObject() {

        @Suppress("unused")
        @ObjCAction
        private fun textViewDidChange(notification: NSNotification) {
            if (textView != notification.`object`) return
            textChangedHandler(textView.text)
        }

        @Suppress("unused")
        @ObjCAction
        private fun textViewDidBeginEditing(notification: NSNotification) {
            if (textView != notification.`object`) return
            if (isPlaceholderShow) {
                textView.text = ""
                textView.textColor = textColor?.toUIColor() ?: UIColor.blackColor
                isPlaceholderShow = false
            }
        }

        @Suppress("unused")
        @ObjCAction
        private fun textViewDidEndEditing(notification: NSNotification) {
            if (textView != notification.`object`) return
            if (textView.text.isEmpty()) {
                textView.text = placeholderText?.localized() ?: ""
                textView.textColor = placeholderColor?.toUIColor() ?: UIColor.grayColor
                isPlaceholderShow = true
            }
        }
    }
}
