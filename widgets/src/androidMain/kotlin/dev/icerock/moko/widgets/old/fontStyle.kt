/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import android.graphics.Typeface
import android.widget.TextView
import dev.icerock.moko.widgets.style.view.FontStyle

fun TextView.applyFontStyle(fontStyle: FontStyle) {
    when (fontStyle) {
        FontStyle.BOLD -> setTypeface(typeface, Typeface.BOLD)
        FontStyle.MEDIUM -> setTypeface(typeface, Typeface.NORMAL)
    }
}