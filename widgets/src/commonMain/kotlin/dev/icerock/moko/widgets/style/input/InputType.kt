/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.input

enum class InputType(val mask: String?) {
    EMAIL(null),
    PLAIN_TEXT(null),
    PASSWORD(null),
    DATE("[00]{.}[00]{.}[0000]"),
    PHONE("+7 ([000]) [000] [00] [00]"),
    DIGITS(null),
    SMS_CODE(null)
}
