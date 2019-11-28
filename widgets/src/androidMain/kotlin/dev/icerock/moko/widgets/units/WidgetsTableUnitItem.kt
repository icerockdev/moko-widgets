/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.view.AspectRatioFrameLayout

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
        val viewBundle = widget.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = lifecycleOwner,
                parent = parent
            )
        )

        val dm = context.resources.displayMetrics
        val (lp, view) = (viewBundle.size to viewBundle.view)
            .toRecyclerViewLayoutParams(dm)

        viewBundle.margins?.let { lp.applyMargin(dm, it) }

        return view.apply {
            layoutParams = lp
        }
    }

    fun Pair<WidgetSize, View>.toRecyclerViewLayoutParams(
        dm: DisplayMetrics
    ): Pair<RecyclerView.LayoutParams, View> {
        val widgetSize = this.first
        val view = this.second
        return when (widgetSize) {
            is WidgetSize.Const<out SizeSpec, out SizeSpec> -> {
                RecyclerView.LayoutParams(
                    widgetSize.width.toPlatformSize(dm),
                    widgetSize.height.toPlatformSize(dm)
                ) to view
            }
            is WidgetSize.AspectByWidth<out SizeSpec> -> {
                val lp = RecyclerView.LayoutParams(
                    widgetSize.width.toPlatformSize(dm),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                lp to AspectRatioFrameLayout(
                    context = view.context,
                    aspectRatio = widgetSize.aspectRatio,
                    aspectByWidth = true
                ).apply {
                    addView(
                        view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
            is WidgetSize.AspectByHeight<out SizeSpec> -> {
                val lp = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    widgetSize.height.toPlatformSize(dm)
                )

                lp to AspectRatioFrameLayout(
                    context = view.context,
                    aspectRatio = widgetSize.aspectRatio,
                    aspectByWidth = false
                ).apply {
                    addView(
                        view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        }
    }

    private class ViewHolder<T>(
        val mutableData: MutableLiveData<T>,
        view: View
    ) : RecyclerView.ViewHolder(view)
}
