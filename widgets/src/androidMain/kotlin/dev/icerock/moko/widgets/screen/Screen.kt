/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.widgets.utils.ThemeAttrs
import dev.icerock.moko.widgets.utils.getIntNullable
import java.util.concurrent.Executor
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

actual abstract class Screen<Arg : Args> : Fragment() {
    private val attachFragmentHooks = mutableListOf<(Fragment) -> Unit>()
    private val activityResultHooks = mutableMapOf<Int, (result: Int, data: Intent?) -> Unit>()

    val routeHandlers = mutableMapOf<Int, (Parcelable?) -> Unit>()

    var requestCode: Int? = null
    var resultCode: Int? = null
    var screenId: Int? = null

    actual open val androidStatusBarColor: Color? = null
    actual open val isLightStatusBar: Boolean? = null

    actual inline fun <reified VM : ViewModel, Key : Any> getViewModel(
        key: Key,
        crossinline viewModelFactory: () -> VM
    ): VM {
        return ViewModelProvider(this, createViewModelFactory { viewModelFactory() })
            .get(key.toString(), VM::class.java)
    }

    actual fun <T : Any> createEventsDispatcher(): EventsDispatcher<T> {
        val mainLooper = Looper.getMainLooper()
        val mainHandler = Handler(mainLooper)
        val mainExecutor = Executor { mainHandler.post(it) }
        return EventsDispatcher(mainExecutor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.run {
            requestCode = getIntNullable(REQUEST_CODE_KEY)
            resultCode = getIntNullable(RESULT_CODE_KEY)
            screenId = getIntNullable(SCREEN_ID_KEY)
        }
    }

    override fun onResume() {
        super.onResume()

        val color = resolveStatusBarColor()
        setStatusBarColor(color)

        val lightStatusBar = resolveIsStatusBarLight()
        setLightStatusBar(lightStatusBar)
    }

    private fun resolveStatusBarColor(): Int {
        if (androidStatusBarColor != null) return androidStatusBarColor!!.argb.toInt()

        var parent = parentFragment
        while (parent != null) {
            if (parent is Screen<*> && parent.androidStatusBarColor != null) {
                return parent.androidStatusBarColor!!.argb.toInt()
            }
            parent = parent.parentFragment
        }

        val hostActivity = activity as? HostActivity
        val appColor = hostActivity?.application?.androidStatusBarColor
        if (appColor != null) return appColor.argb.toInt()

        return ThemeAttrs.getPrimaryDarkColor(requireContext())
    }

    private fun resolveIsStatusBarLight(): Boolean {
        if (isLightStatusBar != null) return isLightStatusBar!!

        var parent = parentFragment
        while (parent != null) {
            if (parent is Screen<*> && parent.isLightStatusBar != null) {
                return parent.isLightStatusBar!!
            }
            parent = parent.parentFragment
        }

        val hostActivity = activity as? HostActivity
        val isLightContent = hostActivity?.application?.isLightStatusBar
        if (isLightContent != null) return isLightContent

        return ThemeAttrs.getLightStatusBar(requireContext())
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

        requireActivity().window.statusBarColor = color
    }

    private fun setLightStatusBar(lightStatusBar: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val view = requireActivity().window.decorView ?: return
        view.systemUiVisibility = if (lightStatusBar) {
            view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        requestCode?.let { outState.putInt(REQUEST_CODE_KEY, it) }
        resultCode?.let { outState.putInt(RESULT_CODE_KEY, it) }
        screenId?.let { outState.putInt(SCREEN_ID_KEY, it) }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        attachFragmentHooks.forEach { it(childFragment) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        activityResultHooks[requestCode]?.let { hook ->
            hook(resultCode, data)
        }
    }

    fun <T> registerAttachFragmentHook(
        value: T,
        hook: (Fragment) -> Unit
    ): ReadOnlyProperty<Screen<*>, T> {
        attachFragmentHooks.add(hook)
        return createConstReadOnlyProperty(value)
    }

    fun <T> registerActivityResultHook(
        requestCode: Int,
        value: T,
        hook: (result: Int, data: Intent?) -> Unit
    ): ReadOnlyProperty<Screen<*>, T> {
        activityResultHooks[requestCode] = hook
        return createConstReadOnlyProperty(value)
    }

    fun <T> createConstReadOnlyProperty(value: T): ReadOnlyProperty<Screen<*>, T> {
        return object : ReadOnlyProperty<Screen<*>, T> {
            override fun getValue(thisRef: Screen<*>, property: KProperty<*>): T {
                return value
            }
        }
    }

    private companion object {
        const val REQUEST_CODE_KEY = "screen:requestCode"
        const val RESULT_CODE_KEY = "screen:resultCode"
        const val SCREEN_ID_KEY = "screen:id"
    }
}
