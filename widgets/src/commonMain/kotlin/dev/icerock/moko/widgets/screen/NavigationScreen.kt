/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import kotlin.reflect.KClass

expect abstract class NavigationScreen() : Screen<Args.Empty> {
    abstract val rootScreen: KClass<out Screen<Args.Empty>>
}