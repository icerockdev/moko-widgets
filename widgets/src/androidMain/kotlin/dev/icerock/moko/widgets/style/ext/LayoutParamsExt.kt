package com.icerockdev.mpp.widgets.style.ext

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

fun ViewGroup.MarginLayoutParams.setDpMargins(
    resources: Resources,
    marginStart: Float = 0.0F,
    marginEnd: Float = 0.0F,
    marginTop: Float = 0.0F,
    marginBottom: Float = 0.0F
) {
    val density = resources.displayMetrics.density

    setMargins(
        (density * marginStart).toInt(),
        (density * marginTop).toInt(),
        (density * marginEnd).toInt(),
        (density * marginBottom).toInt()
    )
    setMarginStart((density * marginStart).toInt())
    setMarginEnd((density * marginEnd).toInt())
}

fun View.setDpPadding(
    resources: Resources,
    paddingStart: Float = 0.0F,
    paddingEnd: Float = 0.0F,
    paddingTop: Float = 0.0F,
    paddingBottom: Float = 0.0F
) {
    val density = resources.displayMetrics.density

    setPaddingRelative(
        (density * paddingStart).toInt(),
        (density * paddingTop).toInt(),
        (density * paddingEnd).toInt(),
        (density * paddingBottom).toInt()
    )
}