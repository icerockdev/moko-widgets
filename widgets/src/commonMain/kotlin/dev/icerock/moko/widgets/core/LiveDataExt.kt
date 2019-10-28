package dev.icerock.moko.widgets.core

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData

fun <T> T.asLiveData(): LiveData<T> = MutableLiveData(initialValue = this)
