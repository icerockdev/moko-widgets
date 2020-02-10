/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.resources.desc.StringDesc

expect fun Screen<*>.showAlertDialog(factory: AlertDialogView.() -> Unit)

expect class AlertDialogView {
    fun title(title: String)
    fun message(message: String)
    fun title(title: StringDesc)
    fun message(message: StringDesc)
    fun positiveButton(title: String, action: () -> Unit)
    fun positiveButton(title: StringDesc, action: () -> Unit)
    fun negativeButton(title: String, action: () -> Unit)
    fun negativeButton(title: StringDesc, action: () -> Unit)
    fun neutralButton(title: String, action: () -> Unit)
    fun neutralButton(title: StringDesc, action: () -> Unit)
}

