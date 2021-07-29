/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import dev.icerock.moko.widgets.core.widget.ContainerWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.ext.applyMargin
import dev.icerock.moko.widgets.core.style.ext.toPlatformSize
import dev.icerock.moko.widgets.core.style.view.Alignment
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.SizeSpec
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.view.AspectRatioFrameLayout

actual class ContainerViewFactory actual constructor(
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<out Fill>?
) : ViewFactory<ContainerWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ContainerWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val root = FrameLayout(context).apply {
            applyBackgroundIfNeeded(this@ContainerViewFactory.background)
            applyPaddingIfNeeded(padding)
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
            margins = margins
        )
    }

    private fun Pair<WidgetSize, View>.toFrameLayoutParams(
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
}
