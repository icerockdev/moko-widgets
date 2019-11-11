/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import com.icerockdev.library.App
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.HostActivity
import dev.icerock.moko.widgets.screen.Screen
import kotlin.reflect.KClass

class MainActivity : HostActivity() {
    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return App.rootScreen
    }
}
