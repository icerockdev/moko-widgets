/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.text.InputFilter.LengthFilter
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.widget.InputLengthWidget

actual open class InputLengthViewFactory actual constructor() : ViewFactory<InputLengthWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: InputLengthWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = widget.child.buildView(viewFactoryContext) as ViewBundle<WS>
        val editText: EditText = getEditText(bundle.view)
            ?: throw IllegalArgumentException("EditText not found in child widget result view")

        widget.maxLength.bind(viewFactoryContext.lifecycleOwner) { maxLength ->
            val filtersWithoutLength = editText.filters.filter { it !is LengthFilter }.toTypedArray()
            if (maxLength == null) {
                editText.filters = filtersWithoutLength
            } else {
                editText.filters = filtersWithoutLength.plus(LengthFilter(maxLength))
            }
        }

        return bundle
    }

    protected open fun getEditText(view: View): EditText? {
        when (view) {
            is EditText -> return view
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    val child = view.getChildAt(i)
                    val editText = getEditText(child)
                    if (editText != null) return editText
                }
                return null
            }
            else -> return null
        }
    }
}
