/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

expect fun Screen<*>.sendEmail(
    email: String,
    subject: String,
    body: String
)
