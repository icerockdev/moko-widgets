/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.utils.bind
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.FocusableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.widget.InputWidget
import dev.icerock.moko.widgets.core.view.FloatingLabelInputView
import platform.UIKit.UITextField
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class FloatingLabelInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    override val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
    private val labelTextStyle: TextStyle<Color>?,
    private val errorTextStyle: TextStyle<Color>?,
    private val underLineColor: FocusableState<Color>?,
    private val textHorizontalAlignment: TextHorizontalAlignment?
) : BaseInputViewFactory<FloatingLabelInputView>(),
    ViewFactory<InputWidget<out WidgetSize>> {

    override fun createTextField(widget: InputWidget<out WidgetSize>): Pair<FloatingLabelInputView, UITextField> {
        val paddingEdges = padding.run {
            Edges(
                top = this?.top?.toDouble() ?: 0.0,
                leading = this?.start?.toDouble() ?: 0.0,
                bottom = this?.bottom?.toDouble() ?: 0.0,
                trailing = this?.end?.toDouble() ?: 0.0
            )
        }

        val inputView = FloatingLabelInputView(paddingEdges).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(this@FloatingLabelInputViewFactory.background)

            applyTextStyleIfNeeded(textStyle)
            applyErrorStyleIfNeeded(errorTextStyle)
            applyLabelStyleIfNeeded(labelTextStyle)
            widget.inputType?.applyTo(this.textField)

            underLineColor?.also {
                deselectedColor = it.unfocused.toUIColor()
                selectedColor = it.focused.toUIColor()
            }

            onFocusLost = {
                widget.field.validate()
            }

            if (textHorizontalAlignment != null) {
                horizontalAlignment = textHorizontalAlignment
            }
        }

        return inputView to inputView.textField
    }

    override fun bindLabel(label: LiveData<StringDesc>, rootView: FloatingLabelInputView, textField: UITextField) {
        label.bind(rootView) { placeholder = it.localized() }
    }

    override fun bindFieldToTextField(
        field: FormField<String, StringDesc>,
        rootView: FloatingLabelInputView,
        textField: UITextField
    ) {
        super.bindFieldToTextField(field, rootView, textField)
        field.data.bind(rootView) {
            if (!textField.isEditing()) {
                rootView.layoutPlaceholder()
            }
        }
        field.error.bind(rootView) { error = it?.localized() }
    }
}
