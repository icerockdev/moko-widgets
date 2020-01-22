/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

fun Float.dp(context: Context): Float = dp(dm = context.resources.displayMetrics)
fun Float.sp(context: Context): Float = sp(dm = context.resources.displayMetrics)

fun Float.dp(dm: DisplayMetrics): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, dm)
fun Float.sp(dm: DisplayMetrics): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, dm)

fun Int.dp(context: Context): Int = toFloat().dp(context).toInt()
