/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.permissions

import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.widgets.core.screen.Screen

actual fun Screen<*>.createPermissionsController(): PermissionsController {
    val appContext = requireContext().applicationContext
    return PermissionsController(applicationContext = appContext)
}

actual fun PermissionsController.bind(screen: Screen<*>) {
    bind(lifecycle = screen.lifecycle, fragmentManager = screen.childFragmentManager)
}
