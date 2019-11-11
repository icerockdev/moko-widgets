/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import kotlin.reflect.KClass

private val screenFactories = mutableMapOf<KClass<Screen<*>>, () -> Screen<*>>()

@Suppress("UNCHECKED_CAST")
fun <A : Args, S : Screen<A>> registerScreenFactory(kClass: KClass<S>, factory: () -> S) {
    screenFactories[kClass as KClass<Screen<*>>] = factory
}

@Suppress("UNCHECKED_CAST")
fun <A : Args, S : Screen<A>> KClass<S>.instantiate(): S {
    val factory = screenFactories[this as KClass<Screen<*>>]
    requireNotNull(factory) { "can't be null!" }
    return factory() as S
}
