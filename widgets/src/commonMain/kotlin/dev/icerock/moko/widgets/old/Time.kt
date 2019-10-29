/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

data class Time(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) {
    override fun toString(): String {
        return "$hours:${minutes.formatTime()}"
    }

    private fun Int.formatTime() = this.toString().padStart(2, '0')
}