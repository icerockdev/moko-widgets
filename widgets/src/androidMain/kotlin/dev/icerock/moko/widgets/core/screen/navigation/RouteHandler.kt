/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen.navigation

import dev.icerock.moko.widgets.core.screen.Screen
import kotlin.properties.ReadOnlyProperty

actual interface RouteHandler<T> {
    val requestCode: Int
}

actual fun <T> Screen<*>.registerRouteHandler(
    code: Int,
    route: RouteWithResult<*, T>,
    handler: (T?) -> Unit
): ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
    routeHandlers[code] = {
        handler(it?.let(route.resultMapper))
    }
    val routeHandler = object : RouteHandler<T> {
        override val requestCode: Int = code
    }
    return createConstReadOnlyProperty(routeHandler)
}
