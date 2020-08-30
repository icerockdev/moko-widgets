/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import dev.icerock.moko.resources.desc.StringDesc
import kotlin.properties.ReadOnlyProperty

expect fun Screen<*>.showAlertDialog(dialogId: Int, factory: AlertDialogBuilder.() -> Unit)

expect class AlertDialogHandler

expect fun Screen<*>.registerAlertDialogHandler(
    positive: ((dialogId: Int) -> Unit)? = null,
    neutral: ((dialogId: Int) -> Unit)? = null,
    negative: ((dialogId: Int) -> Unit)? = null
): ReadOnlyProperty<Screen<*>, AlertDialogHandler>

expect class AlertDialogBuilder {
    fun title(title: StringDesc)
    fun message(message: StringDesc)

    fun positiveButton(title: StringDesc)
    fun neutralButton(title: StringDesc)
    fun negativeButton(title: StringDesc)

    fun handler(handler: AlertDialogHandler)
}
