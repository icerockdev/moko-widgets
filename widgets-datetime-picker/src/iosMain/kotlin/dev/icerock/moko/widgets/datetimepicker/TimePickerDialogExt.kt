/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.datetimepicker

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.screen.Screen
import dev.icerock.moko.widgets.core.utils.setEventHandler
import platform.Foundation.NSBundle
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSDate
import platform.UIKit.UIApplication
import platform.UIKit.UIButton
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIModalPresentationOverCurrentContext
import platform.UIKit.UIView
import platform.UIKit.UIViewController
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

actual class TimePickerDialogHandler(
    val positive: ((dialogId: Int, hour: Int, minute: Int) -> Unit)?,
    val negative: ((dialogId: Int) -> Unit)?
)

actual fun Screen<*>.registerTimePickerDialogHandler(
    positive: ((dialogId: Int, hour: Int, minute: Int) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, TimePickerDialogHandler> {
    return createConstReadOnlyProperty(TimePickerDialogHandler(positive, negative))
}

actual fun Screen<*>.showTimePickerDialog(
    dialogId: Int,
    handler: TimePickerDialogHandler,
    factory: TimePickerDialogBuilder.() -> Unit
) {
    TimePickerDialogBuilder().apply {
        factory(this)
        val controller = createViewController(handler, dialogId)
        controller.modalPresentationStyle = UIModalPresentationOverCurrentContext
        this@showTimePickerDialog.viewController.presentViewController(
            controller,
            animated = true,
            completion = null
        )
    }
}

actual class TimePickerDialogBuilder {
    private var initialHour = 12
    private var initialMinute = 5
    private var accentColor: Color? = null

    actual fun setAccentColor(color: Color) {
        accentColor = color
    }

    actual fun setInitialHour(hour: Int) {
        initialHour = validateHours(hour)
    }

    actual fun setInitialMinutes(minute: Int) {
        initialMinute = validateMinutes(minute)
    }

    /**
     * Don't work for iOS, it takes format from user local settings in setting.app.
     */
    actual fun is24HoursFormat(flag: Boolean) {}

    internal fun createViewController(
        handler: TimePickerDialogHandler,
        dialogId: Int
    ) = TimePickerController(accentColor, initialHour, initialMinute, handler, dialogId)
}

class TimePickerController(
    private val accentColor: Color?,
    private val initialHour: Int,
    private val initialMinute: Int,
    private val handler: TimePickerDialogHandler,
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
        datePicker.datePickerMode = UIDatePickerMode.UIDatePickerModeTime
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

        val initialComponents = NSCalendar.currentCalendar.components(
            NSCalendarUnitHour or NSCalendarUnitMinute,
            NSDate()
        ).apply {
            setHour(initialHour.toLong())
            setMinute(initialMinute.toLong())
        }
        NSCalendar.currentCalendar.dateFromComponents(initialComponents)?.let {
            datePicker.setDate(it)
        }

        doneButton.setEventHandler(UIControlEventTouchUpInside) {
            this.dismissViewControllerAnimated(flag = true, completion = null)
            val hour =
                NSCalendar.currentCalendar.components(NSCalendarUnitHour, datePicker.date).hour
            val minute =
                NSCalendar.currentCalendar.components(NSCalendarUnitMinute, datePicker.date).minute
            handler.positive?.invoke(dialogId, hour.toInt(), minute.toInt())
        }
        cancelButton.setEventHandler(UIControlEventTouchUpInside) {
            this.dismissViewControllerAnimated(flag = true, completion = null)
            handler.negative?.invoke(dialogId)
        }
    }

}
