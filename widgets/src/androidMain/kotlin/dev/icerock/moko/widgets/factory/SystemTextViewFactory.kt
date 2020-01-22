/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.TextView
import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.TextAlignment
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class SystemTextViewFactory actual constructor(
    private val background: Background?,
    private val textStyle: TextStyle?,
    private val textAlignment: TextAlignment?,
    private val margins: MarginValues?
) : ViewFactory<TextWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: TextWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val textView = TextView(context).apply {
            applyTextStyleIfNeeded(textStyle)
            applyBackgroundIfNeeded(this@SystemTextViewFactory.background)

            @SuppressLint("RtlHardcoded")
            when (this@SystemTextViewFactory.textAlignment) {
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
            margins = margins
        )
    }
}
