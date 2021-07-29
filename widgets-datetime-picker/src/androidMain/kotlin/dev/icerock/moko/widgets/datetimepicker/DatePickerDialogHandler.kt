/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.datetimepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.soywiz.klock.DateTime
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.widgets.core.screen.Screen
import java.util.*
import kotlin.properties.ReadOnlyProperty

actual fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    handler: DatePickerDialogHandler,
    factory: DatePickerDialogBuilder.() -> Unit
) {
    val alert = DatePickerDialogBuilder(
        dialogId,
        this.childFragmentManager,
        handler
    )
    factory(alert)
    alert.show()
}

actual class DatePickerDialogHandler

actual fun Screen<*>.registerDatePickerDialogHandler(
    positive: ((dialogId: Int, date: DateTime) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, DatePickerDialogHandler> =
    registerAttachFragmentHook(DatePickerDialogHandler()) { fragment ->
        if (fragment !is DatePickerDialogFragment) return@registerAttachFragmentHook

        fragment.listener = object :
            DatePickerDialogFragment.Listener {
            override fun onPositivePressed(dialogId: Int, date: DateTime) {
                positive?.invoke(dialogId, date)
            }

            override fun onNegativePressed(dialogId: Int) {
                negative?.invoke(dialogId)
            }
        }
    }

actual class DatePickerDialogBuilder(
    private val dialogId: Int,
    private val fragmentManager: FragmentManager,
    private val handler: DatePickerDialogHandler
) {
    private var startDate: DateTime? = null
    private var endDate: DateTime? = null
    private var selectedDate: DateTime? = null

    actual fun accentColor(color: Color) {
        //android color from style
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

    internal fun show() {
        val alertDialogFragment =
            DatePickerDialogFragment.instantiate(
                arg = DatePickerDialogFragment.Args(
                    dialogId = dialogId,
                    startDate = startDate?.unixMillisLong,
                    endDate = endDate?.unixMillisLong,
                    selectedDate = selectedDate?.unixMillisLong
                )
            )
        alertDialogFragment.show(fragmentManager, null)
    }
}

class DatePickerDialogFragment : DialogFragment() {
    var listener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val argument = arguments?.getParcelable<Args>(
            ARG_KEY
        )
        requireNotNull(argument) { "can't be opened without argument" }

        val dialogId = argument.dialogId
        val selectedDate = Calendar.getInstance()
        argument.selectedDate?.let {
            selectedDate.timeInMillis = it
        }
        val dialog = DatePickerDialog(
            requireContext(), { _, year, month, day
                ->
                val date = Calendar.getInstance()
                date.set(year, month, day)
                listener?.onPositivePressed(dialogId, DateTime(date.timeInMillis))
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        argument.endDate?.let { dialog.datePicker.maxDate = it }
        argument.startDate?.let { dialog.datePicker.minDate = it }
        dialog.setOnCancelListener { listener?.onNegativePressed(dialogId) }
        return dialog
    }

    interface Listener {
        fun onPositivePressed(dialogId: Int, date: DateTime)
        fun onNegativePressed(dialogId: Int)
    }

    @Parcelize
    data class Args(
        val dialogId: Int,
        val startDate: Long?,
        val endDate: Long?,
        val selectedDate: Long?
    ) : Parcelable

    companion object {
        private const val ARG_KEY = "arg_bundle"

        fun instantiate(arg: Args): DatePickerDialogFragment {
            return DatePickerDialogFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_KEY, arg)
                    }
                }
        }
    }
}
