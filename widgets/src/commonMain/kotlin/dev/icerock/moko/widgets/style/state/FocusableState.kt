/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.state

data class FocusableState<out T>(
    val focused: T,
    val unfocused: T
) {
    constructor(all: T) : this(unfocused = all, focused = all)
}
