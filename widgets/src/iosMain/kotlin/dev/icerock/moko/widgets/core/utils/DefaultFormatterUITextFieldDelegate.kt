/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.Foundation.NSRange
import platform.Foundation.NSString
import platform.Foundation.create
import platform.Foundation.stringByReplacingCharactersInRange
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIResponder
import platform.UIKit.UIReturnKeyType
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.UIKit.UIView
import platform.UIKit.UIWindow
import platform.UIKit.convertRect
import platform.UIKit.subviews
import platform.UIKit.window
import platform.darwin.NSObject

class DefaultFormatterUITextFieldDelegate(
    private val inputFormatter: DefaultTextFormatter?
) : NSObject(), UITextFieldDelegateProtocol {

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
            .filter { it.canBecomeFirstResponder() }
            .toList()

        val index = fields.indexOf(textField)
        if (index < 0 || index == (fields.count() - 1)) {
            return null
        }
        return fields[index + 1] as? UIView
    }

    private fun fillRespondersList(window: UIWindow): Sequence<UIResponder> {
        val rootView = window.subviews.firstOrNull() as? UIView ?: return emptySequence()
        return respondersSubviews(rootView).sortedBy { item ->
            item as UIView
            item.convertRect(item.frame, toView = rootView).useContents { this.origin.y }
        }.onEach { item ->
            item as UIView
            if(item.canBecomeFirstResponder) {
                val yPos = item.convertRect(item.frame, toView = rootView).useContents { this.origin.y }
                println("$item at pos $yPos")
            }
        }
    }

    private fun respondersSubviews(view: UIView): Sequence<UIResponder> {
        val sequence = view.subviews.asSequence()
        return sequence.filterIsInstance<UIResponder>() +
                sequence.flatMap { respondersSubviews(it as UIView) }
    }
}
