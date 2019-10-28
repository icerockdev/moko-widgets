package dev.icerock.moko.widgets.utils

import android.content.Context

fun Int.dp(context: Context): Int = (context.resources.displayMetrics.density * this).toInt()