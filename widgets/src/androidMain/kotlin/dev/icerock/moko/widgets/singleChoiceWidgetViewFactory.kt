/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.content.res.ColorStateList
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.old.applyStyle
import dev.icerock.moko.widgets.style.background.buildBackground
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.dp

actual var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget> = { viewFactoryContext: ViewFactoryContext,
                                                                      singleChoiceWidget: SingleChoiceWidget ->

    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val dm = context.resources.displayMetrics
    val style = singleChoiceWidget.style

    val container = FrameLayout(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        ).apply {
            applyMargin(context, style.margins)
        }
    }

    val textInputLayout = TextInputLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
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

        applyStyle(style.textStyle)

        inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        isFocusable = false

        style.underlineColor?.also {
            backgroundTintList = ColorStateList.valueOf(it.argb.toInt())
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
            singleChoiceWidget.field.data.value = null
            singleChoiceWidget.field.validate()
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            singleChoiceWidget.field.data.value = position
            singleChoiceWidget.field.validate()
        }
    }

    style.dropDownBackground?.let {
        spinner.setPopupBackgroundDrawable(it.buildBackground(context))
    }

    singleChoiceWidget.field.data.mergeWith(singleChoiceWidget.values) { index, values ->
        if (index == null) null
        else values[index]
    }.bind(lifecycleOwner) { stringDesc ->
        val string = stringDesc?.toString(context)

        if (editText.text?.toString() == string) return@bind

        editText.setText(string)
    }
    singleChoiceWidget.field.error.bind(lifecycleOwner) { error ->
        textInputLayout.error = error?.toString(context)
        textInputLayout.isErrorEnabled = error != null
    }

    singleChoiceWidget.label.bind(lifecycleOwner) { textInputLayout.hint = it?.toString(context) }

    singleChoiceWidget.values.bind(lifecycleOwner) { values ->
        val items = values?.map { it.toString(context) }.orEmpty()
        spinner.adapter = ArrayAdapter(context, R.layout.item_spinner, items)
    }

    container
}
