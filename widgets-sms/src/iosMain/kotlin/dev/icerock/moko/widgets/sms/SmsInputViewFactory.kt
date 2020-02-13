package dev.icerock.moko.widgets.sms

import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import platform.UIKit.UITextField
import platform.UIKit.UITextContentTypeOneTimeCode
import platform.UIKit.UIView
import platform.UIKit.subviews

actual class SmsInputViewFactory actual constructor(private val wrapped: ViewFactory<InputWidget<out WidgetSize>>) :
    ViewFactory<InputWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle =
            wrapped.build(widget = widget, size = size, viewFactoryContext = viewFactoryContext)
        val view = bundle.view

        val textField = findTextField(view)
        if (textField != null) {
            textField.textContentType = UITextContentTypeOneTimeCode
        }
        return bundle
    }

    private fun findTextField(view: UIView): UITextField? {
        return when (view) {
            is UITextField -> view
            else -> {
                view.subviews.forEach {
                    if (it is UIView) {
                        val textField = findTextField(it)
                        if (textField != null) return textField
                    }
                }
                null
            }
        }
    }
}