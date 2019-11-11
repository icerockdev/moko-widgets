/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import platform.Foundation.NSCoder
import platform.UIKit.UIViewController

actual abstract class Screen<Arg : Args> {
    actual inline fun <reified VM : ViewModel, Key : Any> getViewModel(
        key: Key,
        crossinline viewModelFactory: () -> VM
    ): VM {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun <T : Any> createEventsDispatcher(): EventsDispatcher<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun dispatchNavigation(actions: Navigation.() -> Unit) {
    }

    abstract fun createView(): UIViewController
}
