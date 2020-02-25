/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.state

data class CheckableState<out T>(
    val checked: T,
    val unchecked: T
) {
    constructor(all: T) : this(unchecked = all, checked = all)
}
