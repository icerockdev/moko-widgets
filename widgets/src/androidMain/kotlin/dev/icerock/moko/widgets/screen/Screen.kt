/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.widgets.core.View
import java.util.concurrent.Executor

actual abstract class Screen<Arg : Args> : Fragment() {
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

    actual val parentScreen: Screen<*>?
        get() {
            return parentFragment as? Screen<*>
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        return createView(context = requireContext(), parent = container)
    }

    abstract fun createView(context: Context, parent: ViewGroup?): View
}
