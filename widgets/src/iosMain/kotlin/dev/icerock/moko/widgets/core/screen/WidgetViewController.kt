/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.getStatusBarStyle
import dev.icerock.moko.widgets.core.utils.safeSystemBackgroundColor
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.useContents
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSNumber
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSValue
import platform.CoreGraphics.CGPointMake
import platform.UIKit.CGRectValue
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIControl
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIGestureRecognizerDelegateProtocol
import platform.UIKit.UIKeyboardAnimationDurationUserInfoKey
import platform.UIKit.UIKeyboardFrameBeginUserInfoKey
import platform.UIKit.UIKeyboardFrameEndUserInfoKey
import platform.UIKit.UIKeyboardWillChangeFrameNotification
import platform.UIKit.UIKeyboardWillHideNotification
import platform.UIKit.UIKeyboardWillShowNotification
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UITouch
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addGestureRecognizer
import platform.UIKit.addSubview
import platform.UIKit.animateWithDuration
import platform.UIKit.backgroundColor
import platform.UIKit.bottomAnchor
import platform.UIKit.convertRect
import platform.UIKit.endEditing
import platform.UIKit.isDescendantOfView
import platform.UIKit.layoutIfNeeded
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewScrollPosition
import platform.UIKit.UITableView
import platform.UIKit.subviews
import kotlin.math.max

@ExportObjCClass
class WidgetViewController(
    private val isLightStatusBar: Boolean?
) : UIViewController(nibName = null, bundle = null), UIGestureRecognizerDelegateProtocol {

    lateinit var widget: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>

    lateinit var bottomConstraint: NSLayoutConstraint

    private var isScrollListOnKeyboardResize = false

    override fun viewDidLoad() {
        super.viewDidLoad()

        val screen = getAssociatedScreen() as WidgetScreen<*>
        val viewBundle = widget.buildView(this)
        val widgetView = viewBundle.view
        widgetView.translatesAutoresizingMaskIntoConstraints = false
        isScrollListOnKeyboardResize = screen.isScrollListOnKeyboardResize

        with(view) {
            backgroundColor = UIColor.safeSystemBackgroundColor

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
            tapGesture.delegate = this
            view.userInteractionEnabled = true
            view.addGestureRecognizer(tapGesture)
        }
    }

    override fun gestureRecognizer(
        gestureRecognizer: UIGestureRecognizer,
        shouldReceiveTouch: UITouch
    ): Boolean {
        shouldReceiveTouch.view?.let { touchView ->
            if (touchView.isKindOfClass(UIControl.`class`()) && touchView.isDescendantOfView(view)) {
                return false
            }
        }
        return true
    }

    @ObjCAction
    fun updateKeyboardPadding(notification: NSNotification) {
        val info = notification.userInfo ?: return
        val startFrameValue = info[UIKeyboardFrameBeginUserInfoKey] as NSValue
        val endFrameValue = info[UIKeyboardFrameEndUserInfoKey] as NSValue
        val durationNumber = info[UIKeyboardAnimationDurationUserInfoKey] as NSNumber

        val rootView = UIApplication.sharedApplication.keyWindow?.rootViewController?.view ?: view

        val startY = rootView.convertRect(rect = startFrameValue.CGRectValue, toView = view)
            .useContents { origin.y }
        val endY = rootView.convertRect(rect = endFrameValue.CGRectValue, toView = view)
            .useContents { origin.y }
        val screenHeight = view.bounds.useContents { size.height }
        val duration = durationNumber.doubleValue

        val startConstant = max(screenHeight - startY, 0.0)

        val endConstant = max(screenHeight - endY, 0.0)

        val tableView = if (isScrollListOnKeyboardResize) {
            findTableView(view)
        } else {
            null
        }
        val tableViewContentYOffset = tableView?.contentOffset?.useContents {
            this.y
        } ?: 0.0
        val tableViewOldMaxY =
            tableView?.frame?.useContents { this.origin.y + this.size.height } ?: 0.0
        val tableViewHeight = tableView?.frame?.useContents { this.size.height } ?: 0.0

        bottomConstraint.constant = startConstant
        view.layoutIfNeeded()
        UIView.animateWithDuration(duration = duration) {
            bottomConstraint.constant = endConstant
            view.layoutIfNeeded()
        }

        if (isScrollListOnKeyboardResize && tableView != null && notification.name == UIKeyboardWillChangeFrameNotification) {

            val contentHeight = tableView.contentSize.useContents { this.height }
            val tableMaxY = tableView.frame.useContents { this.origin.y + this.size.height }

            val newContentOffset = tableViewContentYOffset + (tableViewOldMaxY - tableMaxY)

            // cant use setContentOffset if newContentOffset greater then current max contentOffset(contentHeight - tableViewHeight)
            if (contentHeight - tableViewHeight > newContentOffset) {
                tableView.setContentOffset(
                    CGPointMake(
                        x = 0.0,
                        y = tableViewContentYOffset + (tableViewOldMaxY - tableMaxY)
                    )
                )
            } else {
                val lastCell = tableView.visibleCells.lastOrNull() as? UITableViewCell
                if (lastCell != null) {
                    tableView.scrollToRowAtIndexPath(
                        tableView.indexPathForCell(lastCell)!!,
                        animated = true,
                        atScrollPosition = UITableViewScrollPosition.UITableViewScrollPositionBottom
                    )
                }
            }
        }
    }

    @ObjCAction
    fun onContentViewTap() {
        view.endEditing(true)
    }

    override fun preferredStatusBarStyle(): UIStatusBarStyle {
        val light = isLightStatusBar ?: BaseApplication.sharedInstance.isLightStatusBar
        return getStatusBarStyle(light) ?: super.preferredStatusBarStyle()
    }

    private fun findTableView(view: UIView): UITableView? {
        return when (view) {
            is UITableView -> view
            else -> {
                view.subviews.forEach {
                    if (it is UIView) {
                        val tableView = findTableView(it)
                        if (tableView != null) return tableView
                    }
                }
                null
            }
        }
    }
}
