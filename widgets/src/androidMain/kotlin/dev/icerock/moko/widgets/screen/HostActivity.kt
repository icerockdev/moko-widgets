/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import dev.icerock.moko.widgets.utils.ThemeAttrs
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
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)

                if (f !is Screen<*>) return

                val color = f.androidStatusBarColor ?: application.androidStatusBarColor
                val argb = color?.argb?.toInt() ?: ThemeAttrs.getPrimaryDarkColor(this@HostActivity)
                setStatusBarColor(argb)
            }
        }, true)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val rootScreen = application.rootScreen
            val screenInstance = rootScreen.instantiate()

            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, screenInstance)
                .commit()
        }
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

        window.statusBarColor = color
    }

    abstract val application: BaseApplication
}
