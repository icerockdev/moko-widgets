/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.ext.toStaggeredGridLayoutManager
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.view.UnitItemDecorator

actual class SystemCollectionViewFactory actual constructor(
    private val orientation: Orientation,
    private val spanCount: Int,
    private val background: Background?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
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
                spanCount,
                orientation.toStaggeredGridLayoutManager()
            )
            adapter = unitsAdapter

            id = widget.id::javaClass.name.hashCode()

            applyPaddingIfNeeded(padding)
            applyBackgroundIfNeeded(this@SystemCollectionViewFactory.background)
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

        return ViewBundle(
            view = resultView,
            size = size,
            margins = margins
        )
    }
}
