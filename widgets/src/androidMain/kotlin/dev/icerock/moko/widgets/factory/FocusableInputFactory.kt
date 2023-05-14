/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.view.ViewGroup
import dev.icerock.moko.widgets.FocusableWidget
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class FocusableInputFactory actual constructor() :
    ViewFactory<FocusableWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: FocusableWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        val focusableView = getFocusableChild(view = bundle.view)
        focusableView?.setOnFocusChangeListener { _, hasFocus ->
            widget.isFocused.value = hasFocus
        }

        return bundle
    }

    private fun getFocusableChild(view: View): View? {
        if (view.isFocusable) {
            return view
        } else if(view is ViewGroup) {
            for (index in 0 until view.childCount) {
                val res = getFocusableChild(
                    view = view.getChildAt(index)
                )
                if(res != null) return res
            }
        }

        return null
    }
}
