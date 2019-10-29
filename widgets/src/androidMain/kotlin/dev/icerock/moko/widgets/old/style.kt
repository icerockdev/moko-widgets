/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import android.widget.TextView
import dev.icerock.moko.widgets.style.view.TextStyle

fun TextView.applyStyle(textStyle: TextStyle) {
    setTextColor(textStyle.color.argb.toInt())
    textSize = textStyle.size.toFloat()
    applyFontStyle(textStyle.fontStyle)
}