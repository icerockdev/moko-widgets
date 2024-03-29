/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.core.utils.bind

fun <T> Value<T>.bind(lambda: (T) -> Unit) {
    val value = this.value
    if (value is LiveData<*>) {
        value.bind { lambda(it as T) }
    } else {
        lambda(value as T)
    }
}
