/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import com.icerockdev.library.MainScreen
import com.icerockdev.library.MainViewModel
import com.icerockdev.library.Theme
import dev.icerock.moko.widgets.core.ScreenActivity

class MainActivity : ScreenActivity<MainViewModel, MainScreen.Args, MainScreen>() {
    override fun createScreen(): MainScreen = MainScreen(Theme)

    override fun getArgs(): MainScreen.Args = MainScreen.Args("test")

    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
}
