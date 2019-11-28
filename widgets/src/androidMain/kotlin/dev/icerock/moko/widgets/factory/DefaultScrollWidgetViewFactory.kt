/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.widget.FrameLayout
import android.widget.ScrollView
import dev.icerock.moko.widgets.ScrollWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class DefaultScrollWidgetViewFactory actual constructor(
    style: Style
) : DefaultScrollWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ScrollWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val scrollView = ScrollView(context).apply {
            applyStyle(style)
        }

        val childBundle = widget.child.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = scrollView
            )
        )
        val childView = childBundle.view
        val childSize = childBundle.size
        val childMargins = childBundle.margins

        scrollView.addView(
            childView,
            FrameLayout.LayoutParams(
                childSize.width.toPlatformSize(dm),
                childSize.height.toPlatformSize(dm)
            ).apply {
                childMargins?.let { applyMargin(dm, it) }
            }
        )

        return ViewBundle(
            view = scrollView,
            size = size,
            margins = style.margins
        )
    }
}