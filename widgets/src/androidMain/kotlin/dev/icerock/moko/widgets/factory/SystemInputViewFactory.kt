/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.ViewCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.applyInputType
import dev.icerock.moko.widgets.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.dp

actual class SystemInputViewFactory actual constructor(
    private val background: Background?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle?,
    private val labelTextStyle: TextStyle?,
    private val errorTextStyle: TextStyle?,
    private val underLineColor: Color?,
    private val underLineFocusedColor: Color?
) : ViewFactory<InputWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val textInputLayout = TextInputLayout(context)
        labelTextStyle?.color?.also {
            val hintColor = ColorStateList.valueOf(it.argb.toInt())
            textInputLayout.defaultHintTextColor = hintColor
        }

        errorTextStyle?.color?.also {
            val errorColor = ColorStateList.valueOf(it.argb.toInt())
            textInputLayout.setErrorTextColor(errorColor)
        }

        textInputLayout.applyBackgroundIfNeeded(background)
        textInputLayout.applyPaddingIfNeeded(padding)

        val editText = TextInputEditText(context).apply {
            id = widget.id::javaClass.name.hashCode()

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // EditText's default background have paddings 4dp, while we not change background to own we just change margins
                // https://stackoverflow.com/questions/31735291/removing-the-left-padding-on-an-android-edittext/44497551
                val dp4 = (-4).dp(context)
                MarginLayoutParamsCompat.setMarginStart(this, dp4)
                MarginLayoutParamsCompat.setMarginEnd(this, dp4)
            }

            applyTextStyleIfNeeded(textStyle)
            widget.inputType?.also { applyInputType(it) }

            val focusedColor = underLineFocusedColor?.argb?.toInt()
            val defaultColor = underLineColor?.argb?.toInt()

            if (focusedColor != null && defaultColor != null) {
                ViewCompat.setBackgroundTintList(
                    this, ColorStateList(
                        arrayOf(
                            intArrayOf(android.R.attr.state_focused),
                            intArrayOf(-android.R.attr.state_focused)
                        ),
                        intArrayOf(
                            focusedColor,
                            defaultColor
                        )
                    )
                )
            } else if (defaultColor != null) {
                ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(defaultColor))
            } else if (focusedColor != null) {
                ViewCompat.setBackgroundTintList(
                    this, ColorStateList(
                        arrayOf(
                            intArrayOf(android.R.attr.state_focused)
                        ),
                        intArrayOf(
                            focusedColor
                        )
                    )
                )
            }


            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) widget.field.validate()
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s == null) return

                    widget.field.data.value = s.toString()
                }
            })
        }

        textInputLayout.addView(editText)

        widget.field.data.bind(lifecycleOwner) { data ->
            if (editText.text?.toString() == data) return@bind

            editText.setText(data)
        }
        widget.field.error.bind(lifecycleOwner) { error ->
            textInputLayout.error = error?.toString(context)
            textInputLayout.isErrorEnabled = error != null
        }

        widget.label.bind(lifecycleOwner) { textInputLayout.hint = it?.toString(context) }
        widget.enabled?.bind(lifecycleOwner) { editText.isEnabled = it == true }
        widget.maxLines?.bind(lifecycleOwner) { maxLines ->
            when (maxLines) {
                null -> editText.setSingleLine(false)
                1 -> editText.setSingleLine(true)
                else -> {
                    editText.setSingleLine(false)
                    editText.maxLines = maxLines
                }
            }
        }

////    binding.setupListeners(widget.field)
//    binding.hint.applyStyle(style.labelTextStyle)
//    binding.error.applyStyle(style.errorTextStyle)

        return ViewBundle(
            view = textInputLayout,
            size = size,
            margins = margins
        )
    }
}
