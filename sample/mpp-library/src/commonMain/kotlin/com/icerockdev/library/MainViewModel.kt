/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel

class MainViewModel(val title: String) : ViewModel() {
    private val _state: MutableLiveData<State<String, String>> = MutableLiveData(initialValue = State.Empty())
    val state: LiveData<State<String, String>> = _state

    fun onChangeStatePressed() {
        when (state.value) {
            is State.Empty -> _state.value = State.Loading()
            is State.Loading -> {
                _state.value = State.Data(data = "hello $title!")
            }
            is State.Data -> _state.value = State.Error(error = "this is error")
            is State.Error -> _state.value = State.Empty()
        }
    }
}