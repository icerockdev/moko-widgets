/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.WidgetSize

actual abstract class WidgetsTableUnitItem<T> actual constructor(
    override val itemId: Long,
    val data: T
) : UnitItem {
    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): UnitItemRoot

    override val viewType: Int by lazy { reuseId.hashCode() }

    override fun createViewHolder(
        parent: ViewGroup,
        lifecycleOwner: LifecycleOwner
    ): RecyclerView.ViewHolder {
        val mutableData: MutableLiveData<T> = MutableLiveData(initialValue = data)
        val widget: Widget<out WidgetSize> = createWidget(mutableData).widget
        val context: Context = parent.context
        val view: View = createView(widget, context, lifecycleOwner, parent)
        return ViewHolder(mutableData, view)
    }

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val widgetViewHolder = viewHolder as ViewHolder<T>
        widgetViewHolder.mutableData.value = data
    }

    private fun createView(
        widget: Widget<out WidgetSize>,
        context: Context,
        lifecycleOwner: LifecycleOwner,
        parent: ViewGroup
    ): View {
        return widget.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = parent
            )
        ).view // TODO apply margins?
    }

    private class ViewHolder<T>(
        val mutableData: MutableLiveData<T>,
        view: View
    ) : RecyclerView.ViewHolder(view)
}
