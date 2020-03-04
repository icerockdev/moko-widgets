/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import dev.icerock.moko.graphics.Color
import kotlinx.android.parcel.Parcelize
import kotlin.properties.ReadOnlyProperty

actual class TimePickerDialogHandler

actual fun Screen<*>.registerTimePickerDialogHandler(
    positive: ((dialogId: Int, hour: Int, minute: Int) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, TimePickerDialogHandler> =
    registerAttachFragmentHook(TimePickerDialogHandler()) { fragment ->
        if (fragment !is TimePickerDialogFragment) return@registerAttachFragmentHook

        fragment.positiveAction = positive
        fragment.negativeAction = negative
    }

actual fun Screen<*>.showTimePickerDialog(
    dialogId: Int,
    handler: TimePickerDialogHandler,
    factory: TimePickerDialogBuilder.() -> Unit
) {
    TimePickerDialogBuilder(dialogId, childFragmentManager).apply {
        factory(this)
        show()
    }
}

actual class TimePickerDialogBuilder(
    private val dialogId: Int,
    private val fragmentManager: FragmentManager
) {
    private var initialHour = 12
    private var initialMinute = 5
    private var is24HoursFormat = true

    /**
     * Don't work for Android, it takes accent color from Theme.
     */
    actual fun setAccentColor(color: Color) {}

    actual fun setInitialHour(hour: Int) {
        initialHour = validateHours(hour)
    }

    actual fun setInitialMinutes(minute: Int) {
        initialMinute = validateMinutes(minute)
    }

    actual fun is24HoursFormat(flag: Boolean) {
        is24HoursFormat = flag
    }

    internal fun show() {
        TimePickerDialogFragment(
            TimePickerDialogFragment.Args(dialogId, initialHour, initialMinute, is24HoursFormat)
        ).show(fragmentManager, null)
    }
}

class TimePickerDialogFragment : DialogFragment() {
    var positiveAction: ((dialogId: Int, hour: Int, minute: Int) -> Unit)? = null
    var negativeAction: ((dialogId: Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments?.getParcelable<Args>(ARG_KEY)
        requireNotNull(args) { "Can't be created without arguments." }

        val dialogId = args.dialogId
        val dialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                positiveAction?.invoke(dialogId, hour, minute)
            },
            args.initialHour,
            args.initialMinute,
            args.is24HoursFormat
        )

        dialog.setOnCancelListener { negativeAction?.invoke(dialogId) }
        return dialog
    }

    @Parcelize
    data class Args(
        val dialogId: Int,
        val initialHour: Int,
        val initialMinute: Int,
        val is24HoursFormat: Boolean
    ) : Parcelable

    companion object {
        private const val ARG_KEY = "arg_bundle"

        operator fun invoke(args: Args) = TimePickerDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_KEY, args)
            }
        }

    }
}
