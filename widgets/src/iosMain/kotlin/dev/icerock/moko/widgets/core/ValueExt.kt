/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.core.utils.bind

@Suppress("UNCHECKED_CAST")
fun <T> Value<T>.bind(lambda: (T) -> Unit) {
    val localValue = value
    if (localValue is LiveData<*>) {
        localValue.bind { lambda(it as T) }
    } else {
        lambda(localValue as T)
    }
}
