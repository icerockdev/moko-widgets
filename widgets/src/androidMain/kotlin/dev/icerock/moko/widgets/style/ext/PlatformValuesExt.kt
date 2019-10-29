/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.ext

import android.util.DisplayMetrics
import android.view.ViewGroup
import dev.icerock.moko.widgets.style.view.SizeSpec

fun Int.toPlatformSize(dm: DisplayMetrics): Int {
    return when (this) {
        SizeSpec.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT
        SizeSpec.AS_PARENT -> ViewGroup.LayoutParams.MATCH_PARENT
        else -> (this * dm.density).toInt()
    }
}