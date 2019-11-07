/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.widget.HorizontalScrollView
import android.widget.ScrollView
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.withSize

actual var scrollWidgetViewFactory: VFC<ScrollWidget> = { viewFactoryContext, widget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val style = widget.style

    val scrollView = if (style.orientation == Orientation.VERTICAL) ScrollView(context)
    else HorizontalScrollView(context)

    with(scrollView) {
        val body = widget.child.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = this
            )
        )

        addView(body)
    }

    scrollView.withSize(style.size).apply { applyStyle(style) }
}