/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import App
import android.app.Application
import dev.icerock.moko.widgets.core.screen.BaseApplication

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        mppApp = App().apply { initialize() }
    }

    companion object {
        lateinit var mppApp: BaseApplication
    }
}
