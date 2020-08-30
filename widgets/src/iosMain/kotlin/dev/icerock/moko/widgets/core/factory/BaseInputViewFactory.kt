/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.shouldChangeCharacters
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind
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
        enabled?.bind { textField.enabled = it }
    }

    protected open fun bindLabel(
        label: LiveData<StringDesc>,
        rootView: V,
        textField: UITextField
    ) {
        label.bind { textField.placeholder = it.localized() }
    }

    protected open fun bindFieldToTextField(
        field: FormField<String, StringDesc>,
        rootView: V,
        textField: UITextField
    ) {
        field.data.bind { newValue ->
            val currentText = textField.text.orEmpty()
            val shouldApplyChange = textField.delegate?.shouldChangeCharacters(
                textField = textField,
                range = NSMakeRange(0.toULong(), currentText.length.toULong()),
                text = newValue
            ) ?: true
            if (shouldApplyChange) {
                textField.text = newValue
            }
        }

        textField.setEventHandler(UIControlEventEditingChanged) {
            val currentValue = field.data.value
            val newValue = textField.text

            if (currentValue != newValue) {
                field.data.value = newValue.orEmpty()
            }
        }
    }
}
