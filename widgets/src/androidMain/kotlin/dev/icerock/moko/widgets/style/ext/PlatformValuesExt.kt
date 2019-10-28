package com.icerockdev.mpp.widgets.style.ext

import android.util.DisplayMetrics
import android.view.ViewGroup
import com.icerockdev.mpp.widgets.style.view.SizeSpec

fun Int.toPlatformSize(dm: DisplayMetrics): Int {
    return when (this) {
        SizeSpec.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT
        SizeSpec.AS_PARENT -> ViewGroup.LayoutParams.MATCH_PARENT
        else -> (this * dm.density).toInt()
    }
}