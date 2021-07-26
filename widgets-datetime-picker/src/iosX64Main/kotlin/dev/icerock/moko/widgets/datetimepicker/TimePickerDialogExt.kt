/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.datetimepicker

import cocoapods.mokoWidgetsDateTimePicker.DateBottomSheetController
import dev.icerock.moko.widgets.core.screen.Screen

actual fun Screen<*>.showTimePickerDialog(
    dialogId: Int,
    handler: TimePickerDialogHandler,
    factory: TimePickerDialogBuilder.() -> Unit
) {
    TimePickerDialogBuilder().apply {
        factory(this)
        val controller = DateBottomSheetController()
        val view = createView(handler = handler, dialogId = dialogId) {
            controller.dismiss()
        }
        controller.showOnViewController(
            vc = this@showTimePickerDialog.viewController,
            withContent = view,
            onDismiss = { handler.negative?.invoke(dialogId) }
        )
    }
}
