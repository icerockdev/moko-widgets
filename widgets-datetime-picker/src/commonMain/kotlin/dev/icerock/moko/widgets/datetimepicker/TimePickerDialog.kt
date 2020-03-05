/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.datetimepicker

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty

expect class TimePickerDialogHandler

expect fun Screen<*>.registerTimePickerDialogHandler(
    positive: ((dialogId: Int, hour: Int, minute: Int) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)? = null
): ReadOnlyProperty<Screen<*>, TimePickerDialogHandler>

expect class TimePickerDialogBuilder {
    fun setAccentColor(color: Color)
    fun setInitialHour(hour: Int)
    fun setInitialMinutes(minute: Int)
    fun is24HoursFormat(flag: Boolean)
}

expect fun Screen<*>.showTimePickerDialog(
    dialogId: Int,
    handler: TimePickerDialogHandler,
    factory: TimePickerDialogBuilder.() -> Unit
)

internal fun validateHours(hour: Int) = when {
    hour < 0 -> 0
    hour > 23 -> 23
    else -> hour
}

internal fun validateMinutes(minute: Int) = when {
    minute < 0 -> 0
    minute > 59 -> 59
    else -> minute
}
