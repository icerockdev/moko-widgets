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
import dev.icerock.moko.widgets.style.view.Sized

fun View.applyStyle(style: Widget.Style) {
    val dm = context.resources.displayMetrics

    if (style is Sized && style is Margined) {
        layoutParams = ViewGroup.MarginLayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        ).apply {
            style.margins?.also { applyMargin(context, it) }
        }
    } else if (style is Sized) {
        layoutParams = ViewGroup.LayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        )
    }

    if (style is Padded) {
        style.padding?.also { applyPadding(it) }
    }

    if (style is Backgrounded) {
        style.background?.also { background = it.buildBackground(context) }
    }
}
