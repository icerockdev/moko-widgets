/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty

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
    return createConstReadOnlyProperty(routeHandler)
}
