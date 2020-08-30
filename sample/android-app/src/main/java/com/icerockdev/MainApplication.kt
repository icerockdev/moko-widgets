/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import App
import androidx.multidex.MultiDexApplication
import dev.icerock.moko.widgets.core.screen.BaseApplication

class MainApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        mppApp = App().apply { initialize() }
    }

    companion object {
        lateinit var mppApp: BaseApplication
    }
}
