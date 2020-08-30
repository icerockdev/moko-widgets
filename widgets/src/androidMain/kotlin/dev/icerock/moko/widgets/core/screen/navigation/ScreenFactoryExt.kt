/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen.navigation

import dev.icerock.moko.widgets.core.screen.Args
import dev.icerock.moko.widgets.core.screen.ScreenFactory

actual fun ScreenFactory<Args.Empty, out NavigationScreen<*>>.createRouter() = NavigationScreen.Router()

actual fun ScreenFactory<Args.Empty, out BottomNavigationScreen>.createRouter() = BottomNavigationScreen.Router()
