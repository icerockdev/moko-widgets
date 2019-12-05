/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import dev.icerock.moko.widgets.ContainerWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.view.AspectRatioFrameLayout

actual class DefaultContainerWidgetViewFactory actual constructor(
    style: Style
) : DefaultContainerWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ContainerWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val root = FrameLayout(context).apply {
            applyStyle(style)
        }

        widget.children.forEach { (childWidget, childAlignment) ->
            val childViewBundle = childWidget.buildView(
                ViewFactoryContext(
                    context = context,
                    lifecycleOwner = lifecycleOwner,
                    parent = root
                )
            )
            val childView = childViewBundle.view
            val childSize = childViewBundle.size
            val childMargins = childViewBundle.margins

            val (lp, sizedView) = (childSize to childView).toFrameLayoutParams(dm)

            @SuppressLint("RtlHardcoded")
            childView.layoutParams = lp.apply {
                gravity = when (childAlignment) {
                    Alignment.CENTER -> Gravity.CENTER
                    Alignment.LEFT -> Gravity.LEFT
                    Alignment.RIGHT -> Gravity.RIGHT
                    Alignment.TOP -> Gravity.TOP
                    Alignment.BOTTOM -> Gravity.BOTTOM
                }

                childMargins?.let { applyMargin(dm, it) }
            }
            root.addView(sizedView)
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = style.margins
        )
    }
}

fun Pair<WidgetSize, View>.toFrameLayoutParams(
    dm: DisplayMetrics
): Pair<FrameLayout.LayoutParams, View> {
    val widgetSize = this.first
    val view = this.second
    return when (widgetSize) {
        is WidgetSize.Const<out SizeSpec, out SizeSpec> -> {
            FrameLayout.LayoutParams(
                widgetSize.width.toPlatformSize(dm),
                widgetSize.height.toPlatformSize(dm)
            ) to view
        }
        is WidgetSize.AspectByWidth<out SizeSpec> -> {
            val lp = FrameLayout.LayoutParams(
                widgetSize.width.toPlatformSize(dm),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            lp to AspectRatioFrameLayout(
                context = view.context,
                aspectRatio = widgetSize.aspectRatio,
                aspectByWidth = true
            ).apply {
                addView(
                    view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
        is WidgetSize.AspectByHeight<out SizeSpec> -> {
            val lp = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                widgetSize.height.toPlatformSize(dm)
            )

            lp to AspectRatioFrameLayout(
                context = view.context,
                aspectRatio = widgetSize.aspectRatio,
                aspectByWidth = false
            ).apply {
                addView(
                    view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }
    }
}
