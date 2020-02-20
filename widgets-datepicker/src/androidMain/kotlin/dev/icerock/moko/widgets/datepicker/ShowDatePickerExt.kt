package dev.icerock.moko.widgets.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.screen.Screen
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadOnlyProperty

actual fun Screen<*>.showDatePickerDialog(
    dialogId: Int,
    factory: DatePickerDialogBuilder.() -> Unit
) {
    val alert = DatePickerDialogBuilder(
        dialogId,
        this
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

actual class DatePickerDialogBuilder(private val dialogId: Int, private val screen: Screen<*>) {
    private val context: Context = screen.requireContext()

    private var format: String = ""
    private var startDate: DateTime? = null
    private var endDate: DateTime? = null

    actual fun dateFormat(format: String) {
        this.format = format
    }

    actual fun accentColor(color: Color) {
        //android color from style
    }

    actual fun handler(handler: DatePickerDialogHandler) {
        // handler is just mark that `Screen` have registered alert handler
    }

    actual fun startDate(date: DateTime) {
        startDate = date
    }

    actual fun endDate(date: DateTime) {
        endDate = date
    }

    internal fun show() {
        val alertDialogFragment =
            DatePickerDialogFragment.instantiate(
                arg = DatePickerDialogFragment.Args(
                    dialogId = dialogId,
                    format = format,
                    startDate = startDate,
                    endDate = endDate
                )
            )
        alertDialogFragment.show(screen.childFragmentManager, null)
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
        val formatter = SimpleDateFormat(argument.format ?: "", Locale.getDefault())
        val currentDate = Calendar.getInstance()
        val dialog = DatePickerDialog(
            context, { _
                       , year
                       , month
                       , day
                ->
                val date = Calendar.getInstance()
                date.set(year, month, day)
                listener?.onPositivePressed(dialogId, DateTime.timeInMillis(date.timeInMillis))
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )
        argument.endDate?.let { dialog.datePicker.maxDate = it.date.time }
        argument.startDate?.let { dialog.datePicker.minDate = it.date.time }
        dialog.setOnCancelListener { listener?.onNegativePressed(dialogId) }
        return dialog
    }

    interface Listener {
        fun onPositivePressed(dialogId: Int, date: DateTime)
        fun onNegativePressed(dialogId: Int)
    }

    data class Args(
        val dialogId: Int,
        val format: String?,
        val startDate: DateTime?,
        val endDate: DateTime?
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong().let {
                if (it == 0L) null
                else DateTime.timeInMillis(it)
            },
            parcel.readLong().let {
                if (it == 0L) null
                else DateTime.timeInMillis(it)
            }
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(dialogId)
            parcel.writeString(format)
            parcel.writeLong(startDate?.date?.time ?: 0L)
            parcel.writeLong(endDate?.date?.time ?: 0L)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Args> {
            override fun createFromParcel(parcel: Parcel): Args {
                return Args(
                    parcel
                )
            }

            override fun newArray(size: Int): Array<Args?> {
                return arrayOfNulls(size)
            }
        }

    }

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

actual sealed class DateTime {

    abstract val date: Date

    actual fun format(format: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    actual class timeInMillis actual constructor(mills: Long) : DateTime() {
        override val date = Date(mills)
    }

    actual class fromString actual constructor(time: String, format: String) : DateTime() {
        override val date: Date = SimpleDateFormat(format, Locale.getDefault()).parse(time)
    }

    actual class now actual constructor() : DateTime() {
        override val date = Date()
    }

}
