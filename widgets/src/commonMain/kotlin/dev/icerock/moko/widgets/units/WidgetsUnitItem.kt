/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.WidgetScope

expect abstract class WidgetsUnitItem<T>(
    itemId: Long,
    data: T,
    widgetScope: WidgetScope = WidgetScope()
) : UnitItem {

    abstract val reuseId: String

    abstract fun createWidget(data: LiveData<T>): AnyWidget

    var widgetScope: WidgetScope

}

fun UnitItem.styled(inScope: WidgetScope): UnitItem {
    return (this as? WidgetsUnitItem<*>)?.updateWidgetScope(inScope) ?: this
}

fun WidgetsUnitItem<*>.updateWidgetScope(widgetScope: WidgetScope): UnitItem {
    this.widgetScope = widgetScope
    return this
}