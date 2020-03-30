/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import dev.icerock.moko.graphics.Color
import kotlin.native.concurrent.ThreadLocal
import kotlin.reflect.KClass

abstract class BaseApplication {
    private val _registeredScreens = mutableMapOf<KClass<*>, ScreenDesc<*>>()
    internal val registeredScreens: Map<KClass<*>, ScreenDesc<*>> = _registeredScreens

    lateinit var rootScreen: ScreenDesc<Args.Empty>
        private set

    open val androidStatusBarColor: Color? = null
    open val isLightStatusBar: Boolean? = null

    protected abstract fun setup(): ScreenDesc<Args.Empty>

    fun initialize() {
        rootScreen = setup()
        sharedInstance = this
    }

    fun <Arg : Args, T : Screen<Arg>> registerScreen(
        kClass: KClass<T>,
        factory: ScreenFactory<Arg, T>.() -> T
    ): TypedScreenDesc<Arg, T> = TypedScreenDesc(factory).also {
        _registeredScreens[kClass] = it
    }

    @ThreadLocal
    companion object {
        lateinit var sharedInstance: BaseApplication
    }
}
