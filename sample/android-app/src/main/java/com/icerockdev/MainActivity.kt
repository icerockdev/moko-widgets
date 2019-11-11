/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.icerockdev.library.Screen
import com.icerockdev.library.getRootScreen

//class MainActivity : ScreenActivity<HostScreen.DummyVM, HostScreen.Args, HostScreen>() {
//    override fun createScreen(): HostScreen = SharedFactory().createMainScreen()
//
//    override fun getArgs(): HostScreen.Args = HostScreen.Args("test")
//
//    override val viewModelClass: Class<HostScreen.DummyVM> = HostScreen.DummyVM::class.java
//}

class HostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val rootScreen = getRootScreen()
            val screenInstance = rootScreen.java.newInstance()

            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, screenInstance)
                .commit()
        }
    }

    override fun onBackPressed() {
        val rootScreen = supportFragmentManager.findFragmentById(android.R.id.content)
        if (rootScreen is Screen<*>) {
            if (rootScreen.onBackPressed()) return
        }

        super.onBackPressed()
    }
}