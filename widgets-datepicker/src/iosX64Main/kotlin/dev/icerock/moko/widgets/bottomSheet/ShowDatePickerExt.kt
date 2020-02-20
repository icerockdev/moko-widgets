/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package dev.icerock.moko.widgets.bottomsheet

import cocoapods.mokoWidgetsBottomSheet.BottomSheetController
import dev.icerock.moko.widgets.datepicker.DatePickerDialogBuilder
import dev.icerock.moko.widgets.datepicker.DatePickerDialogHandler
import dev.icerock.moko.widgets.screen.Screen
import platform.UIKit.UIDatePicker
import kotlin.properties.ReadOnlyProperty

actual fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    factory: DatePickerDialogBuilder.() -> Unit
) {
    BottomSheetController().showOnViewController(
        vc = this.viewController,
        withContent = UIDatePicker(),
        onDismiss = {}
    )
}

actual class DatePickerDialogHandler(
    val positive: ((dialogId: Int, date: String) -> Unit)?,
    val negative: ((dialogId: Int) -> Unit)?
)

actual fun Screen<*>.registerDatePickerDialogHandler(
    positive: ((dialogId: Int, date: String) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, DatePickerDialogHandler> {
    val handler = dev.icerock.moko.widgets.datepicker.DatePickerDialogHandler(
        positive = positive,
        negative = negative
    )
    return createConstReadOnlyProperty(handler)
}

actual class DatePickerDialogBuilder(private val dialogId: Int, val screen: Screen<*>) {

    private var handler: DatePickerDialogHandler? = null
    private var format: String = ""

    actual fun dateFormat(format: String) {
        this.format = format
    }

    actual fun handler(handler: DatePickerDialogHandler) {
        this.handler = handler
    }

    internal fun show() {

    }
}
