/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.old.applyInputType
import dev.icerock.moko.widgets.old.applyStyle
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.dp

actual var inputWidgetViewFactory: VFC<InputWidget> = { viewFactoryContext: ViewFactoryContext,
                                                        inputWidget: InputWidget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val dm = context.resources.displayMetrics
    val style = inputWidget.style

    val textInputLayout = TextInputLayout(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        ).apply {
            applyMargin(context, style.margins)
        }

        style.labelTextStyle.color?.also {
            val hintColor = ColorStateList.valueOf(it.argb.toInt())
            defaultHintTextColor = hintColor
        }

        style.errorTextStyle.color?.also {
            val errorColor = ColorStateList.valueOf(it.argb.toInt())
            setErrorTextColor(errorColor)
        }
    }

    val editText = TextInputEditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            // EditText's default background have paddings 4dp, while we not change background to own we just change margins
            // https://stackoverflow.com/questions/31735291/removing-the-left-padding-on-an-android-edittext/44497551
            marginStart = (-4).dp(context)
            marginEnd = (-4).dp(context)
        }

        applyStyle(style.textStyle)
        inputWidget.inputType?.also { applyInputType(it) }

        style.underLineColor?.also {
            backgroundTintList = ColorStateList.valueOf(it.argb.toInt())
        }

        setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) inputWidget.field.validate()
        }
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null) return

                inputWidget.field.data.value = s.toString()
            }
        })
    }

    textInputLayout.addView(editText)

    inputWidget.field.data.bind(lifecycleOwner) { data ->
        if (editText.text?.toString() == data) return@bind

        editText.setText(data)
    }
    inputWidget.field.error.bind(lifecycleOwner) { error ->
        textInputLayout.error = error?.toString(context)
        textInputLayout.isErrorEnabled = error != null
    }

    inputWidget.label.bind(lifecycleOwner) { textInputLayout.hint = it?.toString(context) }
    inputWidget.enabled?.bind(lifecycleOwner) { editText.isEnabled = it == true }
    inputWidget.maxLines?.bind(lifecycleOwner) { maxLines ->
        when (maxLines) {
            null -> editText.setSingleLine(false)
            1 -> editText.setSingleLine(true)
            else -> {
                editText.setSingleLine(false)
                editText.maxLines = maxLines
            }
        }
    }

    textInputLayout

////    binding.setupListeners(widget.field)
//    binding.hint.applyStyle(style.labelTextStyle)
//    binding.error.applyStyle(style.errorTextStyle)
}
