/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.AnyWidget

expect abstract class WidgetsUnitItem<T>(
    itemId: Long,
    data: T
) : UnitItem {
    abstract val reuseId: String

    abstract fun createWidget(data: LiveData<T>): AnyWidget
}