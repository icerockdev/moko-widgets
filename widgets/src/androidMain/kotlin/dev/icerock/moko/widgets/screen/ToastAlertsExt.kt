/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Context
import android.widget.Toast
import dev.icerock.moko.resources.desc.StringDesc

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

actual fun Screen<*>.showToast(message: StringDesc) {
    context?.toast(message.toString(requireContext()))
}