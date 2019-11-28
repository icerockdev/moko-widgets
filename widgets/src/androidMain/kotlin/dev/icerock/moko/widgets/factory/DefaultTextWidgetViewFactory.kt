/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.TextView
import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.applyTextStyle
import dev.icerock.moko.widgets.style.view.TextAlignment
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class DefaultTextWidgetViewFactory actual constructor(
    style: Style
) : DefaultTextWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: TextWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val textView = TextView(context).apply {
            style.textStyle?.also { applyTextStyle(it) }

            applyStyle(style)

            @SuppressLint("RtlHardcoded")
            when (style.textAlignment) {
                TextAlignment.LEFT -> gravity = Gravity.LEFT
                TextAlignment.CENTER -> gravity = Gravity.CENTER
                TextAlignment.RIGHT -> gravity = Gravity.RIGHT
                null -> {
                }
            }
        }

        widget.text.bind(lifecycleOwner) { textView.text = it?.toString(context) }

        return ViewBundle(
            view = textView,
            size = size,
            margins = style.margins
        )
    }
}