/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.graphics.PorterDuff
import android.widget.ProgressBar
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.widget.ProgressBarWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize

actual class SystemProgressBarViewFactory actual constructor(
    private val margins: MarginValues?,
    private val color: Color?
) : ViewFactory<ProgressBarWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ProgressBarWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context
        val progressBar = ProgressBar(context).apply {
            color?.let {
                indeterminateDrawable.setColorFilter(it.argb.toInt(), PorterDuff.Mode.MULTIPLY)
            }
        }
        return ViewBundle(
            view = progressBar,
            size = size,
            margins = margins
        )
    }
}
