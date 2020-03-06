/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.WidgetSize

actual abstract class WidgetsTableUnitItem<T> actual constructor(
    itemId: Long,
    data: T
) : TableUnitItem {
    private val wrapped = object : WidgetsCollectionUnitItem<T>(
        itemId = itemId,
        data = data
    ) {
        override val reuseId: String get() = this@WidgetsTableUnitItem.reuseId

        override fun createWidget(data: LiveData<T>): Widget<out WidgetSize> {
            return this@WidgetsTableUnitItem.createWidget(data).widget
        }
    }

    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): UnitItemRoot

    override val itemId: Long get() = wrapped.itemId
    override val viewType: Int get() = wrapped.viewType

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        wrapped.bindViewHolder(viewHolder)
    }

    override fun createViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder {
        return wrapped.createViewHolder(parent, lifecycleOwner)
    }
}
