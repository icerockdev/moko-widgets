/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.datetimepicker

import com.soywiz.klock.DateTime
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.screen.Screen
import kotlin.properties.ReadOnlyProperty

expect class DatePickerDialogHandler

expect fun Screen<*>.registerDatePickerDialogHandler(
    positive: ((dialogId: Int, date: DateTime) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)? = null
): ReadOnlyProperty<Screen<*>, DatePickerDialogHandler>

expect class DatePickerDialogBuilder {
    fun accentColor(color: Color)
    fun startDate(date: DateTime)
    fun endDate(date: DateTime)
    fun selectedDate(date: DateTime)
}

expect fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    handler: DatePickerDialogHandler,
    factory: DatePickerDialogBuilder.() -> Unit
)