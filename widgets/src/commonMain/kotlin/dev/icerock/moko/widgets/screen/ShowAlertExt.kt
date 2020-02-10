/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.resources.desc.StringDesc

expect fun Screen<*>.showAlertDialog(factory: AlertDialogBuilder.() -> Unit)

expect class AlertDialogBuilder {
    fun title(title: StringDesc)
    fun message(message: StringDesc)
    fun positiveButton(title: StringDesc, action: () -> Unit)
    fun neutralButton(title: StringDesc, action: () -> Unit)
    fun negativeButton(title: StringDesc, action: () -> Unit)
}
