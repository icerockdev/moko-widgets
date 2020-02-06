/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.resources.desc.StringDesc
import platform.Foundation.*
import platform.UIKit.*

actual fun Screen<*>.showAlertDialog(factory: AlertDialogView.() -> Unit) {
    val alert = AlertDialogView(this)
    factory(alert)
    alert.show()
}

actual class AlertDialogView(val screen: Screen<*>) {
    private var title: String? = null
    private var message: String? = null
    private var actions: List<Action> = listOf()

    actual fun title(title: String) {
        this.title = title
    }

    actual fun message(message: String) {
        this.message = message
    }

    actual fun title(title: StringDesc) {
        this.title = title.localized()
    }

    actual fun message(message: StringDesc) {
        this.message = message.localized()
    }

    actual fun positiveButton(title: String, action: () -> Unit){
        actions = actions.plus(Action(title = title, style = UIAlertActionStyleDefault, action = action))
    }
    actual fun positiveButton(title: StringDesc, action: () -> Unit){
        actions = actions.plus(Action(title = title.localized(), style = UIAlertActionStyleDefault, action = action))
    }
    actual fun negativeButton(title: String, action: () -> Unit) {
        actions = actions.plus(Action(title = title, style = UIAlertActionStyleDestructive, action = action))
    }
    actual fun negativeButton(title: StringDesc, action: () -> Unit) {
        actions = actions.plus(Action(title = title.localized(), style = UIAlertActionStyleDestructive, action = action))
    }
    actual fun neutralButton(title: String, action: () -> Unit) {
        actions = actions.plus(Action(title = title, style = UIAlertActionStyleDefault, action = action))
    }
    actual fun neutralButton(title: StringDesc, action: () -> Unit) {
        actions = actions.plus(Action(title = title.localized(), style = UIAlertActionStyleDefault, action = action))
    }

    internal fun show() {
        val alert = UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        actions.forEach {
            alert.addAction(UIAlertAction.actionWithTitle(title = it.title, style = it.style) { _ ->
                it.action()
            })
        }
        screen.viewController.presentModalViewController(alert, true)
    }

    class Action(val title: String, val style: UIAlertActionStyle, val action: () -> Unit)
}
