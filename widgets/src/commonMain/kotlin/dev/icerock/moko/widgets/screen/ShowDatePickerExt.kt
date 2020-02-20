package dev.icerock.moko.widgets.screen

import com.soywiz.klock.DateTime
import dev.icerock.moko.graphics.Color
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
}

expect fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    factory: DatePickerDialogBuilder.() -> Unit,
    handler: DatePickerDialogHandler
)