package dev.icerock.moko.widgets.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.Widget

actual abstract class WidgetsUnitItem<T> actual constructor(itemId: Long, data: T) : UnitItem {
    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): Widget<*>
}