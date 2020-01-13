/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

interface ScreenDesc<Arg : Args> {
    fun instantiate(): Screen<Arg>
}

class TypedScreenDesc<Arg : Args, T : Screen<Arg>>(
    private val factory: ScreenFactory<Arg, T>.() -> T
) : ScreenDesc<Arg> {
    override fun instantiate(): T = factory.invoke(ScreenFactory())
}
