/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.widgets.core.utils.getAssociatedObject
import dev.icerock.moko.widgets.core.utils.setAssociatedObject
import platform.UIKit.UIViewController
import kotlin.native.ref.WeakReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

actual abstract class Screen<Arg : Args> {
    val viewModelStore = mutableMapOf<Any, ViewModel>()
    // TODO private?
    var arg: Arg? = null

    actual open val androidStatusBarColor: Color? = null
    actual open val isLightStatusBar: Boolean? = null

    actual inline fun <reified VM : ViewModel, Key : Any> getViewModel(
        key: Key,
        crossinline viewModelFactory: () -> VM
    ): VM {
        val stored = viewModelStore[key]
        if (stored != null) return stored as VM

        val created = viewModelFactory()
        viewModelStore[key] = created
        return created
    }

    actual open fun onViewCreated() {

    }

    actual fun <T : Any> createEventsDispatcher(): EventsDispatcher<T> {
        return EventsDispatcher()
    }

    protected abstract fun createViewController(isLightStatusBar: Boolean?): UIViewController

    private var _viewController: WeakReference<UIViewController>? = null
    val viewController: UIViewController
        get() {
            val current = _viewController?.get()
            if (current != null) return current

            val vc = createViewController(isLightStatusBar).also {
                setAssociatedObject(it, this)
            }
            onViewCreated()
            _viewController = WeakReference(vc)
            return vc
        }

    fun <T> createConstReadOnlyProperty(value: T): ReadOnlyProperty<Screen<*>, T> {
        return object : ReadOnlyProperty<Screen<*>, T> {
            override fun getValue(thisRef: Screen<*>, property: KProperty<*>): T {
                return value
            }
        }
    }
}

fun UIViewController.getAssociatedScreen(): Screen<*>? = getAssociatedObject(this) as? Screen<*>
