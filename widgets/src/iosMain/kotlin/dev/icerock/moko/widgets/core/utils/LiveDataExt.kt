/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.mvvm.livedata.LiveData

fun <T> LiveData<T>.bind(setter: (T) -> Unit) {
    setter(value)
    addObserver(setter)
}
