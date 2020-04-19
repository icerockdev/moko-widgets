package dev.icerock.moko.widgets.core.behaviours

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData

/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

interface FocusableWidget {
    val getFocused: LiveData<Boolean>
    val setFocused: MutableLiveData<Boolean>
    var hasNext: Boolean
    var hasPrev: Boolean
}