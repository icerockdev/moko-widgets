/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Intent
import android.net.Uri

actual fun Screen<*>.dialPhone(phone: String) {
    val context = context ?: return

    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    if (intent.resolveActivity(context.packageManager) == null) {
        println("activity for $intent not found")
        return
    }

    startActivity(intent)
}
