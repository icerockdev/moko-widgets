/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.input

expect interface InputType {
    companion object
}

expect fun InputType.Companion.plain(mask: String? = null): InputType
expect fun InputType.Companion.email(mask: String? = null): InputType
expect fun InputType.Companion.phone(mask: String? = "+7 ([000]) [000] [00] [00]"): InputType
expect fun InputType.Companion.password(mask: String? = null): InputType
expect fun InputType.Companion.date(mask: String? = "[00]{.}[00]{.}[0000]"): InputType
expect fun InputType.Companion.digits(mask: String? = null): InputType
