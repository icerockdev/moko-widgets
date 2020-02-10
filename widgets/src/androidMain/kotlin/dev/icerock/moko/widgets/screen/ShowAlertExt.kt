/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import androidx.appcompat.app.AlertDialog
import dev.icerock.moko.resources.desc.StringDesc

actual fun Screen<*>.showAlertDialog(factory: AlertDialogBuilder.() -> Unit) {
    val alert = AlertDialogBuilder(this)
    factory(alert)
    alert.show()
}

actual class AlertDialogBuilder(val screen: Screen<*>) {
    private var title: String? = null
    private var message: String? = null

    private var positiveAction: Action? = null
    private var negativeAction: Action? = null
    private var neutralAction: Action? = null

    actual fun title(title: StringDesc) {
        this.title = title.toString(screen.requireContext())
    }

    actual fun message(message: StringDesc) {
        this.message = message.toString(screen.requireContext())
    }

    actual fun positiveButton(title: StringDesc, action: () -> Unit) {
        positiveAction = Action(title = title.toString(screen.requireContext()), action = action)
    }

    actual fun negativeButton(title: StringDesc, action: () -> Unit) {
        negativeAction = Action(title = title.toString(screen.requireContext()), action = action)
    }

    actual fun neutralButton(title: StringDesc, action: () -> Unit) {
        neutralAction = Action(title = title.toString(screen.requireContext()), action = action)
    }

    internal fun show() {
        AlertDialog.Builder(screen.requireContext()).apply {
            setTitle(title)
            setMessage(message)
            if (positiveAction != null) {
                setPositiveButton(positiveAction?.title) { _, _ ->
                    positiveAction?.action?.invoke()
                }
            }
            if (negativeAction != null) {
                setNegativeButton(negativeAction?.title) { _, _ ->
                    negativeAction?.action?.invoke()
                }
            }
            if (neutralAction != null) {
                setNeutralButton(neutralAction?.title) { _, _ ->
                    neutralAction?.action?.invoke()
                }
            }
        }.show()
    }

    class Action(val title: String, val action: () -> Unit)
}

