/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.widgets.screen.Screen

interface Resultable<R : Parcelable> {
    val screenResult: R?
}

interface RouteWithResult<Arg, T> {
    val resultMapper: (Parcelable) -> T

    fun route(
        source: Screen<*>,
        arg: Arg,
        handler: RouteHandler<T>
    )
}

fun <T> RouteWithResult<Unit, T>.route(source: Screen<*>, handler: RouteHandler<T>) {
    route(source = source, arg = Unit, handler = handler)
}
