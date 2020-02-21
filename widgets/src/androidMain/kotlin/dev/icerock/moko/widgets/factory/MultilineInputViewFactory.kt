/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.MarginLayoutParamsCompat
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.colorInt
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyInputType
import dev.icerock.moko.widgets.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.style.ext.getGravityForTextAlignment
import dev.icerock.moko.widgets.style.view.CornerRadiusValue
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.androidId
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.dp
import kotlin.math.ceil

actual class MultilineInputViewFactory actual constructor(
    private val cornerRadiusValue: CornerRadiusValue?,
    private val borderColor: Color?,
    private val borderWidth: Float?,
    private val backgroundViewColor: Color?,
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val textStyle: TextStyle?,
    private val labelTextColor: Color?,
    private val textHorizontalAlignment: TextHorizontalAlignment?
) : ViewFactory<InputWidget<out WidgetSize>> {

    @SuppressLint("RestrictedApi")
    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val editText = EditText(context).apply {
            applyPaddingIfNeeded(padding)

            val backgroundDrawable = GradientDrawable()
            val scale = context.resources.displayMetrics.density

            backgroundViewColor?.let {
                backgroundDrawable.setColor(it.colorInt())
            }

            cornerRadiusValue?.radius?.let {
                backgroundDrawable.cornerRadius = it * scale
            }

            borderColor?.let {
                backgroundDrawable.setStroke(ceil((borderWidth ?: 1f) * scale).toInt(), it.colorInt())
            }
            background = backgroundDrawable

            id = widget.id.androidId

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

            // If there is any nonnull text alignment argument, then set it to gravity
            // otherwise gravity will be with default value.
            if(textHorizontalAlignment != null) {
                gravity = getGravityForTextAlignment(
                    textHorizontalAlignment = textHorizontalAlignment,
                    textVerticalAlignment = null
                )
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) widget.field.validate()
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s == null) return

                    widget.field.data.value = s.toString()
                }
            })

            // setting hint text color
            labelTextColor?.let { setHintTextColor(it.colorInt()) }
        }

        widget.field.data.bind(lifecycleOwner) { data ->
            if (editText.text?.toString() == data) return@bind

            editText.setText(data)
        }

        widget.label.bind(lifecycleOwner) { editText.hint = it?.toString(context) }
        widget.enabled?.bind(lifecycleOwner) { editText.isEnabled = it == true }
        editText.setSingleLine(false)

        return ViewBundle(
            view = editText,
            size = size,
            margins = margins
        )
    }
}