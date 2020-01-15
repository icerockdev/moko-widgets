/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
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

    val routeHandlers = mutableMapOf<Int, (Parcelable?) -> Unit>()

    var requestCode: Int? = null
    var resultCode: Int? = null
    var screenId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.run {
            requestCode = getIntNullable(REQUEST_CODE_KEY)
            resultCode = getIntNullable(RESULT_CODE_KEY)
            screenId = getIntNullable(SCREEN_ID_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        requestCode?.let { outState.putInt(REQUEST_CODE_KEY, it) }
        resultCode?.let { outState.putInt(RESULT_CODE_KEY, it) }
        screenId?.let { outState.putInt(SCREEN_ID_KEY, it) }
    }

    private companion object {
        const val REQUEST_CODE_KEY = "screen:requestCode"
        const val RESULT_CODE_KEY = "screen:resultCode"
        const val SCREEN_ID_KEY = "screen:id"
    }

    private fun Bundle.getIntNullable(key: String): Int? {
        return if (containsKey(key)) getInt(key)
        else null
    }
}
