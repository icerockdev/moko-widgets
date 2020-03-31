/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.input

sealed class InputType(val mask: String?) {
    class Email(mask: String? = null) : InputType(mask = mask)
    class Phone(mask: String? = "+7 ([000]) [000] [00] [00]") : InputType(mask = mask)
    class Plain(mask: String? = null) : InputType(mask = mask)
    class Password : InputType(mask = null)
    class Date(mask: String? = "[00]{.}[00]{.}[0000]") : InputType(mask = mask)
    class Digits(mask: String? = null) : InputType(mask = mask)
}
