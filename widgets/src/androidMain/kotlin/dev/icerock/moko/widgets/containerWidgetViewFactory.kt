/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.withSize
import dev.icerock.moko.widgets.view.MarginedFrameLayout

actual var containerWidgetViewFactory: VFC<ContainerWidget> = { viewFactoryContext, widget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val style = widget.style

    val root = MarginedFrameLayout(context)

    widget.children.forEach { (childWidget, childAlignment) ->
        val childView = childWidget.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = root
            )
        )

        val clp = childView.layoutParams
        val lp = when (clp) {
            is FrameLayout.LayoutParams -> clp
            is ViewGroup.MarginLayoutParams -> FrameLayout.LayoutParams(clp)
            else -> FrameLayout.LayoutParams(clp)
        }
        lp.gravity = when (childAlignment) {
            Alignment.CENTER -> Gravity.CENTER
            Alignment.LEFT -> Gravity.LEFT
            Alignment.RIGHT -> Gravity.RIGHT
            Alignment.TOP -> Gravity.TOP
            Alignment.BOTTOM -> Gravity.BOTTOM
        }

        childView.layoutParams = lp
        root.addView(childView)
    }

    root.withSize(style.size).apply { applyStyle(style) }
}