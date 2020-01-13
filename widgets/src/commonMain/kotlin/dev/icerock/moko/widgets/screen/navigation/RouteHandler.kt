/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface RouteHandler<T>

class RouteHandlerProperty<T> : ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
    override fun getValue(thisRef: Screen<*>, property: KProperty<*>): RouteHandler<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun <T> Screen<*>.registerRouteHandler(handler: (T?) -> Unit): RouteHandlerProperty<T> {
    TODO()
}
