/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

actual interface RouteHandler<T> {
    fun handleResult(data: T?)
}

actual fun <T> Screen<*>.registerRouteHandler(
    code: Int, // required for android
    route: RouteWithResult<*, T>, // required for android
    handler: (T?) -> Unit
): ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
    val routeHandler = object : RouteHandler<T> {
        override fun handleResult(data: T?) {
            handler(data)
        }
    }
    return object : ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
        override fun getValue(thisRef: Screen<*>, property: KProperty<*>): RouteHandler<T> {
            return routeHandler
        }
    }
}
