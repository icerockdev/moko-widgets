/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import com.icerockdev.library.Factory
import com.icerockdev.library.screen.HostScreen
import dev.icerock.moko.widgets.core.ScreenActivity

class MainActivity : ScreenActivity<HostScreen.DummyVM, HostScreen.Args, HostScreen>() {
    override fun createScreen(): HostScreen = Factory().createMainScreen()

    override fun getArgs(): HostScreen.Args = HostScreen.Args("test")

    override val viewModelClass: Class<HostScreen.DummyVM> = HostScreen.DummyVM::class.java
}
