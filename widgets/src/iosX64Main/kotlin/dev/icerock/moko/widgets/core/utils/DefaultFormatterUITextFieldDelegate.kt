/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSRange
import platform.Foundation.NSString
import platform.Foundation.create
import platform.Foundation.stringByReplacingCharactersInRange
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIReturnKeyType
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.UIKit.UITextView
import platform.UIKit.UITextViewDelegateProtocol
import platform.UIKit.UITextViewTextDidChangeNotification
import platform.UIKit.UIView
import platform.UIKit.UIWindow
import platform.UIKit.convertRect
import platform.UIKit.subviews
import platform.UIKit.window
import platform.darwin.NSObject


actual class DefaultFormatterUITextFieldDelegate actual constructor(
    private val inputFormatter: DefaultTextFormatter?
) : NSObject(), UITextFieldDelegateProtocol, UITextViewDelegateProtocol {

    override fun textView(
        textView: UITextView,
        shouldChangeTextInRange: CValue<NSRange>,
        replacementText: String
    ): Boolean {
        if (inputFormatter == null) {
            return true
        }
        val nsString = NSString.create(string = textView.text)
        val newText = nsString.stringByReplacingCharactersInRange(
            range = shouldChangeTextInRange,
            withString = replacementText
        )
        val unformattedText = inputFormatter.unformat(newText)

        textView.text = inputFormatter.format(unformattedText)
        NSNotificationCenter.defaultCenter.postNotificationName(
            UITextViewTextDidChangeNotification,
            textView
        )
        return false
    }

    override fun textField(
        textField: UITextField,
        shouldChangeCharactersInRange: CValue<NSRange>,
        replacementString: String
    ): Boolean {
        if (inputFormatter == null) {
            return true
        }
        val nsString = NSString.create(string = textField.text ?: "")
        val newText = nsString.stringByReplacingCharactersInRange(
            range = shouldChangeCharactersInRange,
            withString = replacementString
        )
        val unformattedText = inputFormatter.unformat(newText)

        textField.text = inputFormatter.format(unformattedText)
        textField.sendActionsForControlEvents(UIControlEventEditingChanged)
        return false
    }

    override fun textFieldDidBeginEditing(textField: UITextField) {
        textField.returnKeyType = if (nextResponder(textField) != null) {
            UIReturnKeyType.UIReturnKeyNext
        } else {
            UIReturnKeyType.UIReturnKeyDone
        }
    }

    override fun textFieldShouldReturn(textField: UITextField): Boolean {
        val nextResponder = nextResponder(textField)
        if (nextResponder != null) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        return true
    }

    private fun nextResponder(textField: UITextField): UIView? {
        val window = textField.window ?: return null
        val fields = fillRespondersList(window)
            .toList()

        val index = fields.indexOf(textField)
        if (index < 0 || index == (fields.count() - 1)) {
            return null
        }
        return fields[index + 1]
    }

    private fun fillRespondersList(window: UIWindow): Sequence<UIView> {
        val rootView = window.subviews.firstOrNull() as? UIView ?: return emptySequence()
        return respondersSubviews(rootView)
            .filter { it.canBecomeFirstResponder }
            .sortedBy { item ->
                item.convertRect(item.frame, toView = rootView).useContents { this.origin.y }
            }
            .onEach { item ->
                val yPos =
                    item.convertRect(item.frame, toView = rootView).useContents { this.origin.y }
                println("$item at pos $yPos")
            }
    }

    private fun respondersSubviews(view: UIView): Sequence<UIView> {
        val sequence = view.subviews.asSequence()
        return sequence
            .flatMap { respondersSubviews(it as UIView) }
            .plus(view.subviews.asSequence().map { it as UIView })
    }
}
