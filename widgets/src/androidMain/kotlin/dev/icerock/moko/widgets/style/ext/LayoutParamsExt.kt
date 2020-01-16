/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.ext

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.utils.dp

fun ViewGroup.MarginLayoutParams.applyMargin(
    context: Context,
    margin: MarginValues
) {
    setMargins(
        margin.start.dp(context).toInt(),
        margin.top.dp(context).toInt(),
        margin.end.dp(context).toInt(),
        margin.bottom.dp(context).toInt()
    )
}

fun ViewGroup.MarginLayoutParams.applyMargin(
    dm: DisplayMetrics,
    margin: MarginValues
) {
    setMargins(
        margin.start.dp(dm).toInt(),
        margin.top.dp(dm).toInt(),
        margin.end.dp(dm).toInt(),
        margin.bottom.dp(dm).toInt()
    )
}

fun View.applyPadding(padding: PaddingValues) {
    setPadding(
        padding.start.dp(context).toInt(),
        padding.top.dp(context).toInt(),
        padding.end.dp(context).toInt(),
        padding.bottom.dp(context).toInt()
    )
}
