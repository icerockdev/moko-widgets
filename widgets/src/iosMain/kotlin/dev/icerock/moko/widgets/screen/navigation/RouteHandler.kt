/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty

actual interface RouteHandler<T>

actual fun <T> Screen<*>.registerRouteHandler(
    code: Int,
    route: RouteWithResult<*, T>,
    handler: (T?) -> Unit
): ReadOnlyProperty<Screen<*>, RouteHandler<T>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
