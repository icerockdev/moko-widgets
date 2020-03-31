/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package dev.icerock.moko.widgets.datetimepicker

import com.soywiz.klock.DateTime
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.widgets.core.utils.setEventHandler
import kotlin.properties.ReadOnlyProperty
import platform.UIKit.UIModalPresentationOverCurrentContext
import platform.UIKit.UIViewController
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.backgroundColor
import platform.UIKit.addSubview
import platform.UIKit.leadingAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.bottomAnchor
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.safeAreaLayoutGuide
import platform.UIKit.heightAnchor
import platform.UIKit.topAnchor
import platform.UIKit.UIButton
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIApplication
import platform.Foundation.NSBundle
import platform.Foundation.NSDate
import platform.Foundation.NSTimeIntervalSince1970

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

    fun createViewController(
        handler: DatePickerDialogHandler,
        dialogId: Int
    ): DatePickerController {
        return DatePickerController(
            accentColor = accentColor,
            startDate = startDate,
            endDate = endDate,
            selectedDate = selectedDate,
            handler = handler,
            dialogId = dialogId
        )
    }

}

actual fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    handler: DatePickerDialogHandler,
    factory: DatePickerDialogBuilder.() -> Unit
) {
    val builder = DatePickerDialogBuilder()
    factory(builder)
    val controller = builder.createViewController(handler = handler, dialogId = dialogId)
    controller.modalPresentationStyle = UIModalPresentationOverCurrentContext
    this.viewController.presentViewController(controller, animated = true, completion = null)
}

class DatePickerController(
    private val accentColor: Color?,
    private val startDate: DateTime?,
    private val endDate: DateTime?,
    private val selectedDate: DateTime?,
    private val handler: DatePickerDialogHandler,
    private val dialogId: Int
) : UIViewController(nibName = null, bundle = null) {
    override fun viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.7)

        val pickerBackground = UIView()
        view.addSubview(pickerBackground)
        pickerBackground.translatesAutoresizingMaskIntoConstraints = false
        pickerBackground.backgroundColor = UIColor.whiteColor
        pickerBackground.leadingAnchor.constraintEqualToAnchor(
            anchor = view.leadingAnchor
        ).active = true
        pickerBackground.trailingAnchor.constraintEqualToAnchor(
            anchor = view.trailingAnchor
        ).active = true
        pickerBackground.bottomAnchor.constraintEqualToAnchor(
            anchor = view.bottomAnchor
        ).active = true

        val datePicker = UIDatePicker()
        datePicker.translatesAutoresizingMaskIntoConstraints = false
        datePicker.datePickerMode = UIDatePickerMode.UIDatePickerModeDate
        view.addSubview(datePicker)
        datePicker.leadingAnchor.constraintEqualToAnchor(
            anchor = view.leadingAnchor
        ).active = true
        datePicker.trailingAnchor.constraintEqualToAnchor(
            anchor = view.trailingAnchor
        ).active = true
        datePicker.bottomAnchor.constraintEqualToAnchor(
            anchor = view.safeAreaLayoutGuide.bottomAnchor
        ).active = true
        datePicker.heightAnchor.constraintEqualToConstant(232.0)


        pickerBackground.topAnchor.constraintEqualToAnchor(
            anchor = datePicker.topAnchor
        ).active = true


        val controlPanel = UIView()
        controlPanel.translatesAutoresizingMaskIntoConstraints = false
        controlPanel.backgroundColor = UIColor(red = 0.97, green = 0.97, blue = 0.97, alpha = 1.0)
        view.addSubview(controlPanel)
        controlPanel.leadingAnchor.constraintEqualToAnchor(
            anchor = view.leadingAnchor
        ).active = true
        controlPanel.trailingAnchor.constraintEqualToAnchor(
            anchor = view.trailingAnchor
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
            this.dismissViewControllerAnimated(flag = true, completion = null)
            handler.positive?.invoke(
                dialogId,
                datePicker.date.toKlock()
            )
        }
        cancelButton.setEventHandler(UIControlEventTouchUpInside) {
            this.dismissViewControllerAnimated(flag = true, completion = null)
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