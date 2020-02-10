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

actual fun Screen<*>.showAlertDialog(factory: AlertDialogBuilder.() -> Unit) {
    val alert = AlertDialogBuilder(this)
    factory(alert)
    alert.show()
}

actual class AlertDialogBuilder(val screen: Screen<*>) {
    private var title: String? = null
    private var message: String? = null
    private val actions: MutableList<UIAlertAction> = mutableListOf()

    actual fun title(title: StringDesc) {
        this.title = title.localized()
    }

    actual fun message(message: StringDesc) {
        this.message = message.localized()
    }

    actual fun positiveButton(title: StringDesc, action: () -> Unit) {
        actions.add(
            UIAlertAction.actionWithTitle(
                title = title.localized(),
                style = UIAlertActionStyleDefault,
                handler = { action() }
            )
        )
    }

    actual fun negativeButton(title: StringDesc, action: () -> Unit) {
        actions.add(
            UIAlertAction.actionWithTitle(
                title = title.localized(),
                style = UIAlertActionStyleDestructive,
                handler = { action() }
            )
        )
    }

    actual fun neutralButton(title: StringDesc, action: () -> Unit) {
        actions.add(
            UIAlertAction.actionWithTitle(
                title = title.localized(),
                style = UIAlertActionStyleDefault,
                handler = { action() }
            )
        )
    }

    internal fun show() {
        val alert = UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        actions.forEach { alert.addAction(it) }
        screen.viewController.presentModalViewController(alert, true)
    }
}
