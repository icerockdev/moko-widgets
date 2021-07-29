/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package dev.icerock.moko.widgets.datetimepicker

import com.soywiz.klock.DateTime
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.widgets.core.utils.setEventHandler
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSBundle
import platform.Foundation.NSDate
import platform.Foundation.NSTimeIntervalSince1970
import platform.UIKit.UIApplication
import platform.UIKit.UIButton
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.backgroundColor
import platform.UIKit.bottomAnchor
import platform.UIKit.heightAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.safeAreaLayoutGuide
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import kotlin.properties.ReadOnlyProperty

actual class DatePickerDialogHandler(
    val positive: ((dialogId: Int, date: DateTime) -> Unit)?,
    val negative: ((dialogId: Int) -> Unit)?
)

actual fun Screen<*>.registerDatePickerDialogHandler(
    positive: ((dialogId: Int, date: DateTime) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, DatePickerDialogHandler> {
    val handler = DatePickerDialogHandler(
        positive = positive,
        negative = negative
    )
    return createConstReadOnlyProperty(handler)
}

actual class DatePickerDialogBuilder {

    private var accentColor: Color? = null
    private var startDate: DateTime? = null
    private var endDate: DateTime? = null
    private var selectedDate: DateTime? = null

    actual fun accentColor(color: Color) {
        accentColor = color
    }

    actual fun startDate(date: DateTime) {
        startDate = date
    }

    actual fun endDate(date: DateTime) {
        endDate = date
    }

    actual fun selectedDate(date: DateTime) {
        selectedDate = date
    }

    fun createView(
        handler: DatePickerDialogHandler,
        dialogId: Int,
        onDismiss: () -> Unit
    ): DatePickerView {
        return DatePickerView(
            accentColor = accentColor,
            startDate = startDate,
            endDate = endDate,
            selectedDate = selectedDate,
            handler = handler,
            dialogId = dialogId,
            onDismiss = onDismiss
        )
    }

}

class DatePickerView(
    private val accentColor: Color?,
    private val startDate: DateTime?,
    private val endDate: DateTime?,
    private val selectedDate: DateTime?,
    private val handler: DatePickerDialogHandler,
    private val dialogId: Int,
    onDismiss: () -> Unit
) : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 276.0)) {

    init {
        val pickerBackground = UIView()
        addSubview(pickerBackground)
        pickerBackground.translatesAutoresizingMaskIntoConstraints = false
        pickerBackground.backgroundColor = UIColor.whiteColor
        pickerBackground.leadingAnchor.constraintEqualToAnchor(
            anchor = this.leadingAnchor
        ).active = true
        pickerBackground.trailingAnchor.constraintEqualToAnchor(
            anchor = this.trailingAnchor
        ).active = true
        pickerBackground.bottomAnchor.constraintEqualToAnchor(
            anchor = this.bottomAnchor
        ).active = true

        val datePicker = UIDatePicker()
        datePicker.translatesAutoresizingMaskIntoConstraints = false
        datePicker.datePickerMode = UIDatePickerMode.UIDatePickerModeDate
        addSubview(datePicker)
        datePicker.leadingAnchor.constraintEqualToAnchor(
            anchor = this.leadingAnchor
        ).active = true
        datePicker.trailingAnchor.constraintEqualToAnchor(
            anchor = this.trailingAnchor
        ).active = true
        datePicker.bottomAnchor.constraintEqualToAnchor(
            anchor = this.safeAreaLayoutGuide.bottomAnchor
        ).active = true
        datePicker.heightAnchor.constraintEqualToConstant(232.0)


        pickerBackground.topAnchor.constraintEqualToAnchor(
            anchor = datePicker.topAnchor
        ).active = true


        val controlPanel = UIView()
        controlPanel.translatesAutoresizingMaskIntoConstraints = false
        controlPanel.backgroundColor = UIColor(red = 0.97, green = 0.97, blue = 0.97, alpha = 1.0)
        addSubview(controlPanel)
        controlPanel.leadingAnchor.constraintEqualToAnchor(
            anchor = this.leadingAnchor
        ).active = true
        controlPanel.trailingAnchor.constraintEqualToAnchor(
            anchor = this.trailingAnchor
        ).active = true
        controlPanel.bottomAnchor.constraintEqualToAnchor(
            anchor = datePicker.topAnchor
        ).active = true
        controlPanel.heightAnchor.constraintEqualToConstant(44.0)

        val bundle = NSBundle.bundleForClass(UIApplication)
        val doneButton = UIButton()
        doneButton.translatesAutoresizingMaskIntoConstraints = false
        doneButton.setTitle(
            title = bundle.localizedStringForKey(
                key = "Done",
                value = "Done",
                table = null
            ) ?: "Done",
            forState = UIControlStateNormal
        )
        if (accentColor != null) {
            doneButton.setTitleColor(accentColor.toUIColor(), UIControlStateNormal)
        }
        controlPanel.addSubview(doneButton)
        doneButton.trailingAnchor.constraintEqualToAnchor(
            anchor = controlPanel.trailingAnchor,
            constant = -16.0
        ).active = true
        doneButton.bottomAnchor.constraintEqualToAnchor(
            anchor = controlPanel.bottomAnchor
        ).active = true
        doneButton.topAnchor.constraintEqualToAnchor(
            anchor = controlPanel.topAnchor
        ).active = true

        val cancelButton = UIButton()
        cancelButton.translatesAutoresizingMaskIntoConstraints = false
        cancelButton.setTitle(
            title = bundle.localizedStringForKey(
                key = "Cancel",
                value = "Cancel",
                table = null
            ) ?: "Cancel",
            forState = UIControlStateNormal
        )
        if (accentColor != null) {
            cancelButton.setTitleColor(accentColor.toUIColor(), UIControlStateNormal)
        }
        controlPanel.addSubview(cancelButton)
        cancelButton.leadingAnchor.constraintEqualToAnchor(
            anchor = controlPanel.leadingAnchor,
            constant = 16.0
        ).active = true
        cancelButton.bottomAnchor.constraintEqualToAnchor(
            anchor = controlPanel.bottomAnchor
        ).active = true
        cancelButton.topAnchor.constraintEqualToAnchor(
            anchor = controlPanel.topAnchor
        ).active = true

        datePicker.minimumDate = startDate?.toNSDate()
        datePicker.maximumDate = endDate?.toNSDate()

        if (selectedDate != null) {
            datePicker.setDate(selectedDate.toNSDate())
        }

        doneButton.setEventHandler(UIControlEventTouchUpInside) {
            onDismiss.invoke()
            handler.positive?.invoke(
                dialogId,
                datePicker.date.toKlock()
            )
        }
        cancelButton.setEventHandler(UIControlEventTouchUpInside) {
            onDismiss.invoke()
            handler.negative?.invoke(dialogId)
        }
    }
}

internal fun DateTime.toNSDate(): NSDate {
    return NSDate((unixMillis / 1000) - NSTimeIntervalSince1970)
}

internal fun NSDate.toKlock(): DateTime {
    return DateTime(unixMillis = (this.timeIntervalSinceReferenceDate() + NSTimeIntervalSince1970) * 1000)
}