/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.view.Gravity
import android.widget.TextView
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.applyTextStyle
import dev.icerock.moko.widgets.style.view.TextAlignment
import dev.icerock.moko.widgets.utils.bind

actual var textWidgetViewFactory: VFC<TextWidget> = { viewFactoryContext: ViewFactoryContext,
                                                      textWidget: TextWidget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val style = textWidget.style

    val textView = TextView(context).apply {
        applyStyle(style)
        style.textStyle?.also { applyTextStyle(it) }

        when (style.textAlignment) {
            TextAlignment.LEFT -> gravity = Gravity.LEFT
            TextAlignment.CENTER -> gravity = Gravity.CENTER
            TextAlignment.RIGHT -> gravity = Gravity.RIGHT
            null -> {
            }
        }
    }

    textWidget.text.bind(lifecycleOwner) { textView.text = it?.toString(context) }

    textView
}
