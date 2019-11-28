/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import kotlin.reflect.KClass

abstract class BaseApplication : ScreenFactory {
    private val screenFactoryMap = mutableMapOf<KClass<out Screen<*>>, () -> Screen<*>>()

    fun <A : Args, T : Screen<A>> registerScreenFactory(screenClass: KClass<T>, screenFactory: () -> T) {
        screenFactoryMap[screenClass] = screenFactory
    }

    override fun <A : Args, T : Screen<A>> instantiateScreen(screenClass: KClass<T>): T {
        // FIXME Caused by: java.lang.IllegalStateException: screen class com.bumptech.glide.manager.SupportRequestManagerFragment (Kotlin reflection is not available) not registered
        val factory = screenFactoryMap[screenClass] ?: throw IllegalStateException("screen $screenClass not registered")
        return factory.invoke() as T
    }

    abstract fun setup()
    abstract fun getRootScreen(): KClass<out Screen<Args.Empty>>

    fun createRootScreen(): Screen<Args.Empty> {
        return instantiateScreen(getRootScreen())
    }
}

