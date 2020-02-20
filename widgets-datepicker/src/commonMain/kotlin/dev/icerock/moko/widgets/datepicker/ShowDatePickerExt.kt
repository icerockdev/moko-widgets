package dev.icerock.moko.widgets.datepicker

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty

expect class DatePickerDialogHandler

expect fun Screen<*>.registerDatePickerDialogHandler(
    positive: ((dialogId: Int, date: DateTime) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)? = null
): ReadOnlyProperty<Screen<*>, DatePickerDialogHandler>

expect class DatePickerDialogBuilder {
    fun dateFormat(format: String)
    fun handler(handler: DatePickerDialogHandler)
    fun accentColor(color: Color)
    fun startDate(date: DateTime)
    fun endDate(date: DateTime)
}

expect fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    factory: DatePickerDialogBuilder.() -> Unit
)

expect sealed class DateTime {

    fun format(format: String): String

    class timeInMillis(mills: Long) : DateTime

    class fromString(time: String, format: String) :
        DateTime

    class now() : DateTime
}