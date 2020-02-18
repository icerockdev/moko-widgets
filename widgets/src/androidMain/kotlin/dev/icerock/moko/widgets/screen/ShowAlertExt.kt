/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.StringDesc
import kotlin.properties.ReadOnlyProperty

actual fun Screen<*>.showAlertDialog(dialogId: Int, factory: AlertDialogBuilder.() -> Unit) {
    val context = context ?: return
    val alert = AlertDialogBuilder(dialogId, context, childFragmentManager)
    factory(alert)
    alert.show()
}

actual class AlertDialogHandler

actual fun Screen<*>.registerAlertDialogHandler(
    positive: ((dialogId: Int) -> Unit)?,
    neutral: ((dialogId: Int) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, AlertDialogHandler> = registerAttachFragmentHook(AlertDialogHandler()) { fragment ->
    if (fragment !is AlertDialogFragment) return@registerAttachFragmentHook

    fragment.listener = object : AlertDialogFragment.Listener {
        override fun onPositivePressed(dialogId: Int) {
            positive?.invoke(dialogId)
        }

        override fun onNeutralPressed(dialogId: Int) {
            neutral?.invoke(dialogId)
        }

        override fun onNegativePressed(dialogId: Int) {
            negative?.invoke(dialogId)
        }
    }
}

actual class AlertDialogBuilder(
    private val dialogId: Int,
    private val context: Context,
    private val fragmentManager: FragmentManager
) {
    private var title: String? = null
    private var message: String? = null
    private var positiveBtn: String? = null
    private var neutralBtn: String? = null
    private var negativeBtn: String? = null

    actual fun title(title: StringDesc) {
        this.title = title.toString(context)
    }

    actual fun message(message: StringDesc) {
        this.message = message.toString(context)
    }

    actual fun positiveButton(title: StringDesc) {
        positiveBtn = title.toString(context)
    }

    actual fun negativeButton(title: StringDesc) {
        negativeBtn = title.toString(context)
    }

    actual fun neutralButton(title: StringDesc) {
        neutralBtn = title.toString(context)
    }

    actual fun handler(handler: AlertDialogHandler) {
        // handler is just mark that `Screen` have registered alert handler
    }

    internal fun show() {
        val alertDialogFragment = AlertDialogFragment.instantiate(
            arg = AlertDialogFragment.Arg(
                dialogId = dialogId,
                title = title,
                message = message,
                positiveBtn = positiveBtn,
                neutralBtn = neutralBtn,
                negativeBtn = negativeBtn
            )
        )
        alertDialogFragment.show(fragmentManager, null)
    }
}

class AlertDialogFragment : DialogFragment() {
    var listener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val argument = arguments?.getParcelable<Arg>(ARG_KEY)
        requireNotNull(argument) { "can't be opened without argument" }

        val dialogId = argument.dialogId
        return AlertDialog.Builder(requireContext()).apply {
            setTitle(argument.title)
            setMessage(argument.message)

            argument.positiveBtn?.also {
                setPositiveButton(it) { _, _ ->
                    listener?.onPositivePressed(dialogId)
                }
            }
            argument.neutralBtn?.also {
                setNeutralButton(it) { _, _ ->
                    listener?.onNeutralPressed(dialogId)
                }
            }
            argument.negativeBtn?.also {
                setNegativeButton(it) { _, _ ->
                    listener?.onNegativePressed(dialogId)
                }
            }
        }.create()
    }

    // TODO improve this case
    // now using String in arg, but in case of configuration change in AlertDialog will show old values...another version
    // is not allow formatting stringdesc which can be very useful in dialogs.
    @Parcelize
    data class Arg(
        val dialogId: Int,
        val title: String?,
        val message: String?,
        val positiveBtn: String?,
        val neutralBtn: String?,
        val negativeBtn: String?
    ) : Parcelable

    interface Listener {
        fun onPositivePressed(dialogId: Int)
        fun onNeutralPressed(dialogId: Int)
        fun onNegativePressed(dialogId: Int)
    }

    companion object {
        private const val ARG_KEY = "arg_bundle"

        fun instantiate(arg: Arg): AlertDialogFragment {
            return AlertDialogFragment().apply {
                arguments = Bundle().apply { putParcelable(ARG_KEY, arg) }
            }
        }
    }
}
