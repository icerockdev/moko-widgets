/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import platform.UIKit.UIViewController
import kotlin.reflect.KClass

actual abstract class NavigationScreen actual constructor() : Screen<Args.Empty>() {
    actual abstract val rootScreen: KClass<out Screen<Args.Empty>>

    override fun createView(): UIViewController {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}