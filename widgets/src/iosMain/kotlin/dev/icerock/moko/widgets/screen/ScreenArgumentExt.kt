/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.parcelize.Parcelable

actual fun <T : Parcelable> Screen<Args.Parcel<T>>.getArgument(): T {
    val container = requireNotNull(arg) { "argument can't be null" }
    return container.args
}

fun <T : Parcelable> Screen<Args.Parcel<T>>.setArgument(value: T) {
    arg = Args.Parcel(value)
}