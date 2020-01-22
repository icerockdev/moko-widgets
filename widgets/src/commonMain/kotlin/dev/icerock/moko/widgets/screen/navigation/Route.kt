/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.widgets.screen.Screen

interface Route<T> {
    fun route(source: Screen<*>, arg: T)
}

fun Route<Unit>.route(source: Screen<*>) {
    route(source = source, arg = Unit)
}
