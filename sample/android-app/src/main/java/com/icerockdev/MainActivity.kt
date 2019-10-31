/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import com.icerockdev.library.MainViewModel
import com.icerockdev.library.Theme
import com.icerockdev.library.screen.MainScreen
import dev.icerock.moko.widgets.core.ScreenActivity
import dev.icerock.moko.widgets.core.WidgetScope

class MainActivity : ScreenActivity<MainViewModel, MainScreen.Args, MainScreen>() {
    override fun createScreen(): MainScreen = MainScreen(
        widgetScope = WidgetScope(),
        cryptoScope = Theme.cryptoWidgetScope,
        social1Scope = WidgetScope(),
        social2Scope = Theme.socialWidgetScope,
        mcommerceScope = Theme.mcommerceWidgetScope
    )

    override fun getArgs(): MainScreen.Args = MainScreen.Args("test")

    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
}
