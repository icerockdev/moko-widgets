/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.resources.desc.StringDesc
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertActionStyleDestructive
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import kotlin.properties.ReadOnlyProperty

actual fun Screen<*>.showAlertDialog(dialogId: Int, factory: AlertDialogBuilder.() -> Unit) {
    val alert = AlertDialogBuilder(dialogId, this)
    factory(alert)
    alert.show()
}

actual class AlertDialogHandler(
    val positive: ((dialogId: Int) -> Unit)?,
    val neutral: ((dialogId: Int) -> Unit)?,
    val negative: ((dialogId: Int) -> Unit)?
)

actual fun Screen<*>.registerAlertDialogHandler(
    positive: ((dialogId: Int) -> Unit)?,
    neutral: ((dialogId: Int) -> Unit)?,
    negative: ((dialogId: Int) -> Unit)?
): ReadOnlyProperty<Screen<*>, AlertDialogHandler> {
    val handler = AlertDialogHandler(
        positive = positive,
        neutral = neutral,
        negative = negative
    )
    return createConstReadOnlyProperty(handler)
}

actual class AlertDialogBuilder(private val dialogId: Int, val screen: Screen<*>) {
    private var title: String? = null
    private var message: String? = null
    private var positiveBtn: String? = null
    private var neutralBtn: String? = null
    private var negativeBtn: String? = null
    private var handler: AlertDialogHandler? = null

    actual fun title(title: StringDesc) {
        this.title = title.localized()
    }

    actual fun message(message: StringDesc) {
        this.message = message.localized()
    }

    actual fun positiveButton(title: StringDesc) {
        this.positiveBtn = title.localized()
    }

    actual fun neutralButton(title: StringDesc) {
        this.neutralBtn = title.localized()
    }

    actual fun negativeButton(title: StringDesc) {
        this.negativeBtn = title.localized()
    }

    actual fun handler(handler: AlertDialogHandler) {
        this.handler = handler
    }

    internal fun show() {
        val alert = UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        positiveBtn?.also { title ->
            alert.addAction(
                UIAlertAction.actionWithTitle(
                    title = title,
                    style = UIAlertActionStyleDefault,
                    handler = {
                        handler?.positive?.invoke(dialogId)
                    }
                )
            )
        }
        neutralBtn?.also { title ->
            alert.addAction(
                UIAlertAction.actionWithTitle(
                    title = title,
                    style = UIAlertActionStyleDefault,
                    handler = {
                        handler?.neutral?.invoke(dialogId)
                    }
                )
            )
        }
        negativeBtn?.also { title ->
            alert.addAction(
                UIAlertAction.actionWithTitle(
                    title = title,
                    style = UIAlertActionStyleDestructive,
                    handler = {
                        handler?.negative?.invoke(dialogId)
                    }
                )
            )
        }

        screen.viewController.presentModalViewController(alert, true)
    }
}
