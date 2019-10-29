/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import android.content.Context

fun Int.dp(context: Context): Int = (context.resources.displayMetrics.density * this).toInt()
fun Float.dp(context: Context): Int = (context.resources.displayMetrics.density * this).toInt()