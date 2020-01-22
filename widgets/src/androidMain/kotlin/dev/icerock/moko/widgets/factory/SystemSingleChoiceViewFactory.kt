/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.content.res.ColorStateList
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.core.view.ViewCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.widgets.R
import dev.icerock.moko.widgets.SingleChoiceWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.buildBackground
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.dp

actual class SystemSingleChoiceViewFactory actual constructor(
    private val textStyle: TextStyle?,
    private val labelTextStyle: TextStyle?,
    private val dropDownTextColor: Color?,
    private val underlineColor: Color?,
    private val dropDownBackground: Background?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<SingleChoiceWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: SingleChoiceWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val container = FrameLayout(context)
        container.applyPaddingIfNeeded(padding)

        val textInputLayout = TextInputLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        container.addView(textInputLayout)

        val editText = TextInputEditText(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // EditText's default background have paddings 4dp, while we not change background to own we just change margins
                // https://stackoverflow.com/questions/31735291/removing-the-left-padding-on-an-android-edittext/44497551
                marginStart = (-4).dp(context)
                marginEnd = 4.dp(context)
            }

            applyTextStyleIfNeeded(textStyle)

            inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            isFocusable = false

            underlineColor?.also {
                ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(it.argb.toInt()))
            }
        }

        textInputLayout.addView(editText)

        val spinner = Spinner(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            visibility = View.INVISIBLE
            dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT
        }

        editText.setOnClickListener { spinner.performClick() }

        container.addView(spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                widget.field.data.value = null
                widget.field.validate()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                widget.field.data.value = position
                widget.field.validate()
            }
        }

        dropDownBackground?.let {
            spinner.setPopupBackgroundDrawable(it.buildBackground(context))
        }

        widget.field.data.mergeWith(widget.values) { index, values ->
            if (index == null) null
            else values[index]
        }.bind(lifecycleOwner) { stringDesc ->
            val string = stringDesc?.toString(context)

            if (editText.text?.toString() == string) return@bind

            editText.setText(string)
        }
        widget.field.error.bind(lifecycleOwner) { error ->
            textInputLayout.error = error?.toString(context)
            textInputLayout.isErrorEnabled = error != null
        }

        widget.label.bind(lifecycleOwner) { textInputLayout.hint = it?.toString(context) }

        widget.values.bind(lifecycleOwner) { values ->
            val items = values?.map { it.toString(context) }.orEmpty()
            spinner.adapter = ArrayAdapter(context, R.layout.item_spinner, items)
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = margins
        )
    }
}
