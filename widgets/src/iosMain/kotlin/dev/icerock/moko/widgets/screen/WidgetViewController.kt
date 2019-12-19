/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.useContents
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSNumber
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSValue
import platform.UIKit.CGRectValue
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIKeyboardAnimationDurationUserInfoKey
import platform.UIKit.UIKeyboardFrameBeginUserInfoKey
import platform.UIKit.UIKeyboardFrameEndUserInfoKey
import platform.UIKit.UIKeyboardWillChangeFrameNotification
import platform.UIKit.UIKeyboardWillHideNotification
import platform.UIKit.UIKeyboardWillShowNotification
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addGestureRecognizer
import platform.UIKit.addSubview
import platform.UIKit.animateWithDuration
import platform.UIKit.backgroundColor
import platform.UIKit.bottomAnchor
import platform.UIKit.endEditing
import platform.UIKit.layoutIfNeeded
import platform.UIKit.leadingAnchor
import platform.UIKit.systemBackgroundColor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.window

@ExportObjCClass
class WidgetViewController : UIViewController(nibName = null, bundle = null) {

    lateinit var widget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
    lateinit var screen: WidgetScreen<*>

    lateinit var bottomConstraint: NSLayoutConstraint

    override fun viewDidLoad() {
        super.viewDidLoad()

        val viewBundle = widget.buildView(this)
        val widgetView = viewBundle.view
        widgetView.translatesAutoresizingMaskIntoConstraints = false

        with(view) {
            backgroundColor = UIColor.systemBackgroundColor

            addSubview(widgetView)

            widgetView.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
            widgetView.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
            widgetView.topAnchor.constraintEqualToAnchor(topAnchor).active = true
            bottomConstraint = bottomAnchor.constraintEqualToAnchor(widgetView.bottomAnchor).apply {
                active = true
            }
        }

        if (screen.isKeyboardResizeContent) {
            val nc = NSNotificationCenter.defaultCenter
            nc.addObserver(
                observer = this,
                selector = NSSelectorFromString("updateKeyboardPadding:"),
                name = UIKeyboardWillShowNotification,
                `object` = null
            )
            nc.addObserver(
                observer = this,
                selector = NSSelectorFromString("updateKeyboardPadding:"),
                name = UIKeyboardWillHideNotification,
                `object` = null
            )
            nc.addObserver(
                observer = this,
                selector = NSSelectorFromString("updateKeyboardPadding:"),
                name = UIKeyboardWillChangeFrameNotification,
                `object` = null
            )
        }

        if (screen.isDismissKeyboardOnTap) {
            val tapGesture = UITapGestureRecognizer(
                target = this,
                action = NSSelectorFromString("onContentViewTap")
            )
            tapGesture.cancelsTouchesInView = false
            view.userInteractionEnabled = true
            view.addGestureRecognizer(tapGesture)
        }
    }

    @ObjCAction
    fun updateKeyboardPadding(notification: NSNotification) {
        val info = notification.userInfo ?: return
        val startFrameValue = info[UIKeyboardFrameBeginUserInfoKey] as NSValue
        val endFrameValue = info[UIKeyboardFrameEndUserInfoKey] as NSValue
        val durationNumber = info[UIKeyboardAnimationDurationUserInfoKey] as NSNumber

        val screenHeight = view.window?.bounds?.useContents { size.height } ?: 0.0
        val startY = startFrameValue.CGRectValue.useContents { origin.y }
        val endY = endFrameValue.CGRectValue.useContents { origin.y }
        val duration = durationNumber.doubleValue

        bottomConstraint.constant = (screenHeight - startY)
        view.layoutIfNeeded()
        UIView.animateWithDuration(duration = duration) {
            bottomConstraint.constant = (screenHeight - endY)
            view.layoutIfNeeded()
        }
    }

    @ObjCAction
    fun onContentViewTap() {
        view.endEditing(true)
    }
}
