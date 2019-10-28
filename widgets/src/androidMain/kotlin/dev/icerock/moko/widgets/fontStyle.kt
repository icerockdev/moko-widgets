package com.icerockdev.mpp.widgets.forms

import android.graphics.Typeface
import android.widget.TextView
import com.icerockdev.mpp.widgets.style.view.FontStyle

fun TextView.applyFontStyle(fontStyle: FontStyle) {
    when (fontStyle) {
        FontStyle.BOLD -> setTypeface(typeface, Typeface.BOLD)
        FontStyle.MEDIUM -> setTypeface(typeface, Typeface.NORMAL)
    }
}