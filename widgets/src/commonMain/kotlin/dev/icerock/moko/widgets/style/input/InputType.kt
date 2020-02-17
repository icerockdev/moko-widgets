/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.input

sealed class InputType(open val mask: String?) {
    class Email(override val mask: String? = null) : InputType(mask = mask)
    class Phone(override val mask: String? = "+7 ([000]) [000] [00] [00]") : InputType(mask = mask)
    class Plain(override val mask: String? = null) : InputType(mask = null)
    class Password : InputType(mask = null)
    class Date(override val mask: String? = "[00]{.}[00]{.}[0000]") : InputType(mask = mask)
    class Digits(override val mask: String? = null) : InputType(mask = mask)
}