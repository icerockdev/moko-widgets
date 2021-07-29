/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.widget.LinearLayout
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.ext.applyMargin
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.VerticalPageWidget

actual open class VerticalPageViewFactory :
    ViewFactory<VerticalPageWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: VerticalPageWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        val childContext = viewFactoryContext.copy(parent = container)

        widget.header?.let { header ->
            val childBundle = header.buildView(childContext)
            container.addView(childBundle.view)
        }

        val bodyBundle = widget.body.buildView(childContext)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        ).apply {
            bodyBundle.margins?.let { applyMargin(context, it) }
        }
        container.addView(bodyBundle.view, layoutParams)

        widget.footer?.let { footer ->
            val childBundle = footer.buildView(childContext)
            container.addView(childBundle.view)
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = null
        )
    }
}
