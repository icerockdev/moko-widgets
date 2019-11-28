/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.applyPadding
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class DefaultListWidgetViewFactory actual constructor(
    style: Style
) : DefaultListWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ListWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val haveSwipeRefreshListener = widget.onRefresh != null

        val unitsAdapter = UnitsRecyclerViewAdapter(lifecycleOwner)
        val recyclerView = RecyclerView(context).apply {
            clipToPadding = false

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, style.reversed)
            adapter = unitsAdapter

            if (style.dividerEnabled == true) {
                val dividerDecoration =
                    DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                addItemDecoration(dividerDecoration)
            }
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
            unitsAdapter.units = when (widget.onReachEnd) {
                null -> list
                else -> list.observedEnd(widget.onReachEnd)
            }
        }

        with(resultView) {
            applyStyle(style)
            if (resultView != recyclerView) {
                style.padding?.let { recyclerView.applyPadding(it) }
                resultView.setPadding(0, 0, 0, 0)
            }
        }

        return ViewBundle(
            view = resultView,
            size = size,
            margins = style.margins
        )
    }

    private fun List<TableUnitItem>.observedEnd(onReachEnd: () -> Unit): List<TableUnitItem> {
        if (this.isEmpty()) return this

        val lastWrapped = TableUnitItemWrapper(
            item = this.last(),
            onBind = onReachEnd
        )
        return this.dropLast(1).plus(lastWrapped)
    }

    private class TableUnitItemWrapper(
        private val item: TableUnitItem,
        private val onBind: () -> Unit
    ) : TableUnitItem by item {
        override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
            item.bindViewHolder(viewHolder)
            onBind()
        }
    }
}