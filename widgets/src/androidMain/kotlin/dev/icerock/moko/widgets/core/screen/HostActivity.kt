/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import kotlin.reflect.KClass

abstract class HostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                val kclass = Class.forName(className, true, classLoader).kotlin
                val screenKlass = kclass as? KClass<out Screen<Args.Empty>>
                val screenDesc = screenKlass?.let { application.registeredScreens[it] }
                return screenDesc?.instantiate() ?: super.instantiate(classLoader, className)
            }
        }
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val rootScreen = application.rootScreen
            val screenInstance = rootScreen.instantiate()

            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, screenInstance)
                .commit()
        }
    }

    abstract val application: BaseApplication
}
