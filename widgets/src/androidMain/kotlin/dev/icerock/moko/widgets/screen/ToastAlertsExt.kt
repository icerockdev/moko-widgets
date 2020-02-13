/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.widget.Toast
import dev.icerock.moko.resources.desc.StringDesc

actual fun Screen<*>.showToast(message: StringDesc) {
    context?.let { context ->
        val text = message.toString(context)
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}