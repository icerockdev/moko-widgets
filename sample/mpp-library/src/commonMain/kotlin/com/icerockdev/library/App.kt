/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import com.icerockdev.library.sample.MyBottomNavigationScreen
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.Screen
import kotlin.reflect.KClass

object App {
    val rootScreen: KClass<out Screen<Args.Empty>> = MyBottomNavigationScreen::class
}
