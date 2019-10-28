package com.icerockdev.library

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(title: String) : ViewModel() {
    private val _state: MutableLiveData<State<String, String>> = MutableLiveData(initialValue = State.Empty())
    val state: LiveData<State<String, String>> = _state

    init {
        viewModelScope.launch {
            while (true) {
                delay(2500)

                when (state.value) {
                    is State.Empty -> _state.value = State.Loading()
                    is State.Loading -> {
                        _state.value = State.Data(data = "hello $title!")
                        delay(5000)
                    }
                    is State.Data -> _state.value = State.Error(error = "this is error")
                    is State.Error -> _state.value = State.Empty()
                }
            }
        }
    }
}