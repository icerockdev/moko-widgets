/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

actual abstract class PlatformScreen<A : Args, T> actual constructor(
    private val deps: Deps<A, T>
) : Screen<A>() {
    actual abstract fun createPlatformBundle(): T

    private val platformBundle by lazy { createPlatformBundle() }

    actual interface Deps<A : Args, T> {
        fun createView(
            screen: PlatformScreen<A, T>,
            platformBundle: T,
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return deps.createView(
            screen = this,
            platformBundle = platformBundle,
            inflater = inflater,
            container = container,
            savedInstanceState = savedInstanceState
        )
    }
}
