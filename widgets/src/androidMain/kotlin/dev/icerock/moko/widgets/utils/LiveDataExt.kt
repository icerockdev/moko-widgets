/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

fun <T> dev.icerock.moko.mvvm.livedata.LiveData<T>.bind(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) {
    observer(value)

    this.ld().observe(lifecycleOwner, Observer { value ->
        observer(value)
    })
}

fun <T> dev.icerock.moko.mvvm.livedata.LiveData<T>.bindNotNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    bind(lifecycleOwner) { value ->
        if (value == null) return@bind

        observer(value)
    }
}