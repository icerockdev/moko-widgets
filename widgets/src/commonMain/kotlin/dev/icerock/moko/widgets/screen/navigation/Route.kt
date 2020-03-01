/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

interface Route<T> {
    fun route(arg: T)
}

fun Route<Unit>.route() {
    route(arg = Unit)
}
