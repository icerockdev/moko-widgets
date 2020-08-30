/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect abstract class WidgetsCollectionUnitItem<T>(
    itemId: Long,
    data: T
) : CollectionUnitItem {
    abstract val reuseId: String

    abstract fun createWidget(data: LiveData<T>): Widget<out WidgetSize>
}
