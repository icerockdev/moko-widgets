package dev.icerock.moko.widgets.factory

import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.FocusableWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.objc.setAssociatedObject
import dev.icerock.moko.widgets.style.view.WidgetSize
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldTextDidBeginEditingNotification
import platform.UIKit.UITextFieldTextDidEndEditingNotification
import platform.UIKit.UIView
import platform.UIKit.isDescendantOfView
import platform.darwin.NSObject
import kotlin.native.ref.WeakReference

actual class FocusableInputFactory actual constructor() :
    ViewFactory<FocusableWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: FocusableWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        val bundle = widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        val notificationObserver = EditingEventsHandler(
            focusedLiveData = widget.isFocused,
            view = bundle.view
        )
        setAssociatedObject(bundle.view, notificationObserver)
        val nc = NSNotificationCenter.defaultCenter
        nc.addObserver(
            observer = notificationObserver,
            selector = NSSelectorFromString("didBeginEditing:"),
            name = UITextFieldTextDidBeginEditingNotification,
            `object` = null
        )
        nc.addObserver(
            observer = notificationObserver,
            selector = NSSelectorFromString("didEndEditing:"),
            name = UITextFieldTextDidEndEditingNotification,
            `object` = null
        )
        return bundle
    }

    @ExportObjCClass
    private class EditingEventsHandler(
        val focusedLiveData: MutableLiveData<Boolean>,
        view: UIView
    ): NSObject() {

        private val weakView = WeakReference(view)

        @ObjCAction
        fun didBeginEditing(notification: NSNotification) {
            println("DID BEGIN EDIT")
            val view = weakView.get() ?: return
            val textField = notification.`object` as? UITextField
            textField?.apply {
                if (textField.isDescendantOfView(view)) {
                    focusedLiveData.value = true
                }
            }
        }

        @ObjCAction
        fun didEndEditing(notification: NSNotification) {
            println("DID END EDIT")

            val view = weakView.get() ?: return
            val textField = notification.`object` as? UITextField
            textField?.apply {
                if (textField.isDescendantOfView(view)) {
                    focusedLiveData.value = false
                }
            }
        }

    }
}

