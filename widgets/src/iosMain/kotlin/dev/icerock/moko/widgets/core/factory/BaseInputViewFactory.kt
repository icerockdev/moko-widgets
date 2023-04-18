/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.fields.livedata.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.utils.bind
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.widget.InputWidget
import platform.Foundation.NSMakeRange
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UITextField
import platform.UIKit.UIView

abstract class BaseInputViewFactory<V : UIView> : ViewFactory<InputWidget<out WidgetSize>> {
    protected abstract val margins: MarginValues?

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val (rootView, textField) = createTextField(widget)

        bindEnabled(widget.enabled, rootView, textField)
        bindLabel(widget.label, rootView, textField)
        bindFieldToTextField(widget.field, rootView, textField)

        return ViewBundle(
            view = rootView,
            size = size,
            margins = margins
        )
    }

    protected abstract fun createTextField(
        widget: InputWidget<out WidgetSize>
    ): Pair<V, UITextField>

    protected open fun bindEnabled(
        enabled: LiveData<Boolean>?,
        rootView: V,
        textField: UITextField
    ) {
        if (enabled == null) return
        textField.bind(enabled) { this.enabled = it }
    }

    protected open fun bindLabel(
        label: LiveData<StringDesc>,
        rootView: V,
        textField: UITextField
    ) {
        textField.bind(label) { this.placeholder = it.localized() }
    }

    protected open fun bindFieldToTextField(
        field: FormField<String, StringDesc>,
        rootView: V,
        textField: UITextField
    ) {
        textField.bind(field.data) { newValue ->
            val currentText = this.text.orEmpty()
            val shouldApplyChange = this.delegate?.run {
                textField(
                    this@bind,
                    NSMakeRange(0.toULong(), currentText.length.toULong()), newValue
                )
            } ?: true
            if (shouldApplyChange) {
                this.text = newValue
            }
        }

        textField.setEventHandler(UIControlEventEditingChanged) {
            val currentValue: String = field.value()
            val newValue: String? = textField.text

            if (currentValue != newValue) {
                field.setValue(newValue.orEmpty())
            }
        }
    }
}
