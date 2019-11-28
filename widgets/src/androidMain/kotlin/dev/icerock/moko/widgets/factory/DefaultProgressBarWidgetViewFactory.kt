/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.graphics.PorterDuff
import android.widget.ProgressBar
import dev.icerock.moko.widgets.ProgressBarWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class DefaultProgressBarWidgetViewFactory actual constructor(
    style: Style
) : DefaultProgressBarWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ProgressBarWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context
        val progressBar = ProgressBar(context).apply {
            style.color?.let {
                indeterminateDrawable.setColorFilter(it.argb.toInt(), PorterDuff.Mode.MULTIPLY)
            }

            applyStyle(style)
        }
        return ViewBundle(
            view = progressBar,
            size = size,
            margins = style.margins
        )
    }
}