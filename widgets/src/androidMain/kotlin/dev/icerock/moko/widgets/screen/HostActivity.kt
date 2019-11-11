/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

abstract class HostActivity : AppCompatActivity() {
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

    abstract fun getRootScreen(): KClass<out Screen<Args.Empty>>
}