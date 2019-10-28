package com.icerockdev

import com.icerockdev.library.MainScreen
import com.icerockdev.library.MainViewModel
import dev.icerock.moko.widgets.core.ScreenActivity
import dev.icerock.moko.widgets.core.WidgetScope

class MainActivity : ScreenActivity<MainViewModel, MainScreen.Args, MainScreen>() {
    override fun createScreen(): MainScreen = MainScreen(WidgetScope())

    override fun getArgs(): MainScreen.Args = MainScreen.Args("test")

    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
}
