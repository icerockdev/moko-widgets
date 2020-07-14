/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style

import android.widget.Button
import android.widget.TextView
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.style.state.PressableState
import dev.icerock.moko.widgets.core.style.view.TextStyle

fun Button.applyTextStyleIfNeeded(textStyle: TextStyle<PressableState<Color>>?) {
    applyCommonTextStyleIfNeeded(textStyle)

    if (textStyle == null) return

    textStyle.color?.also { setTextColor(it.toStateList()) }
}

fun TextView.applyTextStyleIfNeeded(textStyle: TextStyle<Color>?) {
    applyCommonTextStyleIfNeeded(textStyle)

    if (textStyle == null) return

    textStyle.color?.also { setTextColor(it.argb.toInt()) }
}

fun TextView.applyCommonTextStyleIfNeeded(textStyle: TextStyle<*>?) {
    if (textStyle == null) return

    textStyle.size?.also { textSize = it.toFloat() }
    textStyle.font?.also { this.setTypeface( it.getTypeface(context))  }
    textStyle.fontStyle?.also { applyFontStyle(it) }
}
