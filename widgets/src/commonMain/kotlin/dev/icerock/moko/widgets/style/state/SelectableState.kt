/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.state

data class SelectableState<out T>(
    val selected: T,
    val unselected: T
) {
    constructor(all: T) : this(selected = all, unselected = all)
}
