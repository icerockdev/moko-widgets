/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import androidx.lifecycle.LifecycleOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.widgets.utils.bind

fun <T> Value<T>.bind(lifecycleOwner: LifecycleOwner, lambda: (T) -> Unit) {
    if (value is LiveData<*>) {
        value.bind(lifecycleOwner) {
            lambda(it as T)
        }
    } else {
        lambda(value as T)
    }
}
