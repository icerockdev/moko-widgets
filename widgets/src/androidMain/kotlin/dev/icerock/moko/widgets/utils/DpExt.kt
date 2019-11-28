/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import android.content.Context
import android.util.DisplayMetrics

fun Int.dp(context: Context): Int = dp(dm = context.resources.displayMetrics)
fun Float.dp(context: Context): Int = dp(dm = context.resources.displayMetrics)

fun Int.dp(dm: DisplayMetrics): Int = (dm.density * this).toInt()
fun Float.dp(dm: DisplayMetrics): Int = (dm.density * this).toInt()