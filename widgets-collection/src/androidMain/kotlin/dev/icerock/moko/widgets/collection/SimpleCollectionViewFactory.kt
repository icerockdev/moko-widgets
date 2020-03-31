/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.collection

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.background.Orientation
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.androidId
import dev.icerock.moko.widgets.core.utils.bind

actual class SimpleCollectionViewFactory actual constructor(
    private val orientation: Orientation,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<Fill.Solid>?
) : ViewFactory<CollectionWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: CollectionWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val haveSwipeRefreshListener = widget.onRefresh != null

        val unitsAdapter = UnitsRecyclerViewAdapter(lifecycleOwner)
        val recyclerView = RecyclerView(context).apply {
            clipToPadding = false
            layoutManager = StaggeredGridLayoutManager(
                1, // in columned collection will be changed
                orientation.toStaggeredGridLayoutManager()
            )
            adapter = unitsAdapter

            id = widget.id.androidId

            applyPaddingIfNeeded(padding)
            applyBackgroundIfNeeded(this@SimpleCollectionViewFactory.background)
        }

        val resultView: View = if (haveSwipeRefreshListener) {
            val swipeRefreshLayout = SwipeRefreshLayout(context).apply {
                clipToPadding = false

                setOnRefreshListener {
                    widget.onRefresh?.invoke {
                        isRefreshing = false
                    }
                }
            }

            swipeRefreshLayout.addView(recyclerView)

            swipeRefreshLayout
        } else {
            recyclerView
        }

        widget.items.bind(lifecycleOwner) { units ->
            val list = units.orEmpty()
            val onReachEnd = widget.onReachEnd
            unitsAdapter.units = when {
                onReachEnd == null -> list
                list.isEmpty() -> list
                else -> list.subList(0, list.lastIndex) + UnitItemDecorator(list.last()) {
                    onReachEnd.invoke()
                }
            }
        }

        return ViewBundle(
            view = resultView,
            size = size,
            margins = margins
        )
    }

    private fun Orientation.toStaggeredGridLayoutManager(): Int = when (this) {
        Orientation.VERTICAL -> StaggeredGridLayoutManager.VERTICAL
        Orientation.HORIZONTAL -> StaggeredGridLayoutManager.HORIZONTAL
    }

    private class UnitItemDecorator(
        private val decorated: UnitItem,
        val onBind: () -> Unit
    ) : UnitItem {

        override val itemId: Long get() = decorated.itemId

        override val viewType: Int get() = decorated.viewType

        override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
            decorated.bindViewHolder(viewHolder)
            onBind()
        }

        override fun createViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder {
            return decorated.createViewHolder(parent, lifecycleOwner)
        }
    }
}
