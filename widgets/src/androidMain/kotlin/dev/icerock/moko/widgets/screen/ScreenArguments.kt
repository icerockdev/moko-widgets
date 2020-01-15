/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.os.Bundle
import dev.icerock.moko.parcelize.Parcelable

private const val sScreenArgsParameter = "args"

actual fun <T : Parcelable> Screen<Args.Parcel<T>>.getArgument(): T {
    return requireNotNull(arguments?.getParcelable(sScreenArgsParameter)) {
        "screen with arguments should have notnull args"
    }
}

fun <T : Parcelable> Screen<Args.Parcel<T>>.setArgument(arg: T) {
    unsafeSetScreenArgument(this, arg)
}

// required for NavigationScreen arguments in routing
fun unsafeSetScreenArgument(screen: Screen<*>, arg: Parcelable) {
    screen.arguments = Bundle().apply {
        putParcelable(sScreenArgsParameter, arg)
    }
}
