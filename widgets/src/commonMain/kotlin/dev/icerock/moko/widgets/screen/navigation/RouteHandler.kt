/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.widgets.screen.Screen
import kotlin.properties.ReadOnlyProperty

expect interface RouteHandler<T>

expect fun <T> Screen<*>.registerRouteHandler(
    code: Int,
    route: RouteWithResult<*, T>,
    handler: (T?) -> Unit
): ReadOnlyProperty<Screen<*>, RouteHandler<T>>
