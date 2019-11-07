/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style

import android.view.View
import android.view.ViewGroup
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.buildBackground
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.applyPadding
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.StateBackgrounded
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.view.AspectRatioFrameLayout

fun View.applyStyle(style: Widget.Style) {
    if (style is Margined) {
        style.margins?.also {
            (layoutParams as? ViewGroup.MarginLayoutParams)?.applyMargin(context, it)
        }
    }

    if (style is Padded) {
        style.padding?.also { applyPadding(it) }
    }

    if (style is Backgrounded) {
        style.background?.also { background = it.buildBackground(context) }
    }

    if (style is StateBackgrounded) {
        style.background?.also { background = it.buildBackground(context) }
    }
}

fun View.withSize(size: WidgetSize): View {
    val dm = context.resources.displayMetrics

    val aspectRatioFrameLayout = when (size) {
        is WidgetSize.Const -> {
            layoutParams = ViewGroup.MarginLayoutParams(
                size.width.toPlatformSize(dm),
                size.height.toPlatformSize(dm)
            )
            return this
        }
        is WidgetSize.AspectByWidth -> {
            AspectRatioFrameLayout(
                context = context,
                aspectRatio = size.aspectRatio,
                aspectByWidth = true
            ).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    size.width.toPlatformSize(dm),
                    0
                )
            }
        }
        is WidgetSize.AspectByHeight -> {
            AspectRatioFrameLayout(
                context = context,
                aspectRatio = size.aspectRatio,
                aspectByWidth = false
            ).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    0,
                    size.height.toPlatformSize(dm)
                )
            }
        }
    }

    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    aspectRatioFrameLayout.addView(this)

    return aspectRatioFrameLayout
}
