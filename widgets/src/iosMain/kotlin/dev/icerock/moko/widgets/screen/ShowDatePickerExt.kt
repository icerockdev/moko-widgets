/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package dev.icerock.moko.widgets.screen


import com.soywiz.klock.DateTime
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty

actual class DatePickerDialogHandler

actual fun Screen<*>.registerDatePickerDialogHandler(
    positive: ((dialogId: Int, date: DateTime) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, DatePickerDialogHandler> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual class DatePickerDialogBuilder {

    actual fun accentColor(color: Color) {
    }

    actual fun startDate(date: DateTime) {
    }

    actual fun endDate(date: DateTime) {
    }
}

actual fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    factory: DatePickerDialogBuilder.() -> Unit,
    handler: DatePickerDialogHandler
) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
