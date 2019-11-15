/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import App
import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        App.setup()
    }
}