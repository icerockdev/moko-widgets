/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.widgets.objc.getAssociatedObject
import dev.icerock.moko.widgets.objc.setAssociatedObject
import platform.UIKit.UIViewController
import kotlin.native.ref.WeakReference

actual abstract class Screen<Arg : Args> {
    val viewModelStore = mutableMapOf<Any, ViewModel>()
    // TODO private?
    var arg: Arg? = null

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

    actual fun <T : Any> createEventsDispatcher(): EventsDispatcher<T> {
        return EventsDispatcher()
    }

    protected abstract fun createViewController(): UIViewController

    private var _viewController: WeakReference<UIViewController>? = null
    val viewController: UIViewController
        get() {
            val current = _viewController?.get()
            if (current != null) return current

            val vc = createViewController().also {
                setAssociatedObject(it, this)
            }
            _viewController = WeakReference(vc)
            return vc
        }
}

fun UIViewController.getAssociatedScreen(): Screen<*>? = getAssociatedObject(this) as? Screen<*>
