/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.state

data class PressableState<out T>(
    val normal: T,
    val disabled: T,
    val pressed: T
) {
    constructor(all: T) : this(normal = all, disabled = all, pressed = all)
}
