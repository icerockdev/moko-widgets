/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import kotlin.reflect.KClass

abstract class BaseApplication {
    protected abstract fun setup(): ScreenDesc<Args.Empty>

    fun initialize() {
        rootScreen = setup()
    }

    lateinit var rootScreen: ScreenDesc<Args.Empty>
        private set

    fun <Arg : Args, T : Screen<Arg>> registerScreen(
        kClass: KClass<T>,
        factory: ScreenFactory<Arg, T>.() -> T
    ): TypedScreenDesc<Arg, T> = TypedScreenDesc(factory).also {
        _registeredScreens[kClass] = it
    }

    private val _registeredScreens = mutableMapOf<KClass<*>, ScreenDesc<*>>()
    internal val registeredScreens: Map<KClass<*>, ScreenDesc<*>> = _registeredScreens
}
