/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import platform.UIKit.UIViewController

// here we can setup own create UIViewController logic
actual class PlatformProfileScreen actual constructor(
    private val deps: Deps
) : ProfileScreen() {

    override fun createViewController(): UIViewController {
        return deps.createViewController(this)
    }

    // creating of UIViewController will be on Swift side
    actual interface Deps {
        fun createViewController(platformProfileScreen: PlatformProfileScreen): UIViewController
    }
}
