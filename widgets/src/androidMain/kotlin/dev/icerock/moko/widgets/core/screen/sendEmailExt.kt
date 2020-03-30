/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import android.content.Intent
import android.net.Uri

actual fun Screen<*>.sendEmail(
    email: String,
    subject: String,
    body: String
) {
    val context = context ?: return

    val intent = Intent(
        Intent.ACTION_SENDTO,
        Uri.fromParts(
            "mailto",
            email,
            null
        )
    )
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, body)
    if (intent.resolveActivity(context.packageManager) == null) {
        println("email clients not found")
        return
    }

    startActivity(Intent.createChooser(intent, email))
}
