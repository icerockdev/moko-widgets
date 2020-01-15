/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

actual interface RouteHandler<T> {
    val requestCode: Int
}

class RouteHandlerProperty<T>(
    private val requestCode: Int
) : ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
    override fun getValue(thisRef: Screen<*>, property: KProperty<*>): RouteHandler<T> {
        return object : RouteHandler<T> {
            override val requestCode: Int = this@RouteHandlerProperty.requestCode
        }
    }
}

actual fun <T> Screen<*>.registerRouteHandler(
    code: Int,
    route: RouteWithResult<*, T>,
    handler: (T?) -> Unit
): ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
    routeHandlers[code] = {
        handler(it?.let(route.resultMapper))
    }
    return RouteHandlerProperty(
        requestCode = code
    )
}
