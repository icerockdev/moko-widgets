/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.MarginLayoutParamsCompat
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.colorInt
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.ext.getGravityForTextAlignment
import dev.icerock.moko.widgets.core.style.view.IOSFieldBorderStyle
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.TextVerticalAlignment
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.androidId
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.dp
import dev.icerock.moko.widgets.core.widget.InputWidget

@Suppress("LongParameterList")
actual class SystemInputViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle<Color>?,
    private val labelTextColor: Color?,
    private val textHorizontalAlignment: TextHorizontalAlignment?,
    private val textVerticalAlignment: TextVerticalAlignment?,
    iosFieldBorderStyle: IOSFieldBorderStyle? // For ios only
) : ViewFactory<InputWidget<out WidgetSize>> {

    @SuppressLint("RestrictedApi")
    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        @Suppress("MagicNumber")
        val editText = AppCompatEditText(context).apply {
            applyBackgroundIfNeeded(this@SystemInputViewFactory.background)
            applyPaddingIfNeeded(padding)

            id = widget.id.androidId

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // EditText's default background have paddings 4dp,
                // while we not change background to own we just change margins
                // https://stackoverflow.com/questions/31735291/removing-the-left-padding-on-an-android-edittext/44497551
                val dp4 = (-4).dp(context)
                MarginLayoutParamsCompat.setMarginStart(this, dp4)
                MarginLayoutParamsCompat.setMarginEnd(this, dp4)
            }

            applyTextStyleIfNeeded(textStyle)
            widget.inputType?.applyTo(this)

            // If there is any nonnull text alignment argument, then set it to gravity
            // otherwise gravity will be with default value.
            if (textHorizontalAlignment != null || textVerticalAlignment != null) {
                gravity = getGravityForTextAlignment(
                    textHorizontalAlignment = textHorizontalAlignment,
                    textVerticalAlignment = textVerticalAlignment
                )
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) widget.field.validate()
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s == null) return

                    widget.field.setValue(s.toString())
                }
            })

            // setting hint text color
            labelTextColor?.let { setHintTextColor(it.colorInt()) }
        }

        widget.field.observeData(lifecycleOwner) { data ->
            if (editText.text?.toString() == data) return@observeData

            editText.setText(data)
        }

        widget.label.bind(lifecycleOwner) { editText.hint = it?.toString(context) }
        widget.enabled?.bind(lifecycleOwner) { editText.isEnabled = it == true }
        widget.maxLines?.bind(lifecycleOwner) { maxLines ->
            when (maxLines) {
                null -> editText.isSingleLine = false
                1 -> editText.isSingleLine = true
                else -> {
                    editText.isSingleLine = false
                    editText.maxLines = maxLines
                }
            }
        }

        return ViewBundle(
            view = editText,
            size = size,
            margins = margins
        )
    }
}
