/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.mvvm.livedata.LiveData

@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS", "EXPERIMENTAL_FEATURE_WARNING")
inline class Value<T> private constructor(val value: Any?) {
    companion object {
        fun <T> data(data: T) = Value<T>(value = data)
        fun <T> liveData(liveData: LiveData<T>) =
            Value<T>(value = liveData)
    }
}
