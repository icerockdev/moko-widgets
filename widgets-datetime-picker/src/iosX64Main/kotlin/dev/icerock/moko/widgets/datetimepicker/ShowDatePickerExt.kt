/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package dev.icerock.moko.widgets.datetimepicker

import cocoapods.mokoWidgetsDateTimePicker.DateBottomSheetController
import dev.icerock.moko.widgets.core.screen.Screen

actual fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    handler: DatePickerDialogHandler,
    factory: DatePickerDialogBuilder.() -> Unit
) {
    val builder = DatePickerDialogBuilder()
    factory(builder)
    val controller = DateBottomSheetController()
    val view = builder.createView(handler = handler, dialogId = dialogId) {
        controller.dismiss()
    }
    controller.showOnViewController(
        vc = this.viewController,
        withContent = view,
        onDismiss = { handler.negative?.invoke(dialogId) }
    )
}
