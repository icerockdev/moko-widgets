/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.ext

import android.content.Context
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
        margin.start.dp(context),
        margin.top.dp(context),
        margin.end.dp(context),
        margin.bottom.dp(context)
    )
}

fun View.applyPadding(padding: PaddingValues) {
    setPadding(
        padding.start.dp(context),
        padding.top.dp(context),
        padding.end.dp(context),
        padding.bottom.dp(context)
    )
}
