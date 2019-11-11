/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel

expect abstract class Screen<Arg : Args> {
    inline fun <reified VM : ViewModel, Key : Any> getViewModel(key: Key, crossinline viewModelFactory: () -> VM): VM

    fun <T : Any> createEventsDispatcher(): EventsDispatcher<T>

    fun dispatchNavigation(actions: Navigation.() -> Unit)
}

inline fun <Arg : Args, reified VM : ViewModel> Screen<Arg>.getViewModel(crossinline viewModelFactory: () -> VM): VM {
    return getViewModel(Unit, viewModelFactory)
}

expect fun <T : Parcelable> Screen<Args.Parcel<T>>.getArgument(): T
