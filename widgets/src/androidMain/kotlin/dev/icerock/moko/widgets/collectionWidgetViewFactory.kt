/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.toStaggeredGridLayoutManager
import dev.icerock.moko.widgets.style.withSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.view.UnitItemDecorator

actual var collectionWidgetViewFactory: VFC<CollectionWidget> = { viewFactoryContext, widget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val style = widget.style
    val dm = context.resources.displayMetrics

    val haveSwipeRefreshListener = widget.onRefresh != null

    val unitsAdapter = UnitsRecyclerViewAdapter(lifecycleOwner)
    val recyclerView = RecyclerView(context).apply {
        clipToPadding = false
        layoutManager = StaggeredGridLayoutManager(
            widget.style.spanCount,
            widget.style.orientation.toStaggeredGridLayoutManager()
        )
        adapter = unitsAdapter
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
        unitsAdapter.units = when {
            widget.onReachEnd == null -> list
            list.isEmpty() -> list
            else -> list.subList(0, list.lastIndex) + UnitItemDecorator(list.last()) {
                widget.onReachEnd.invoke()
            }
        }
    }

    resultView.withSize(widget.layoutParams.size).apply { applyStyle(style) }
}