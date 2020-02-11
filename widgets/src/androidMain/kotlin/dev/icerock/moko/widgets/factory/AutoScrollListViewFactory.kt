/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class AutoScrollListViewFactory actual constructor(
    private val listViewFactory: ViewFactory<ListWidget<out WidgetSize>>,
    private val isAlwaysAutoScroll: Boolean
) : ViewFactory<ListWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ListWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = listViewFactory.build(widget, size, viewFactoryContext)
        val view = bundle.view

        val recyclerView: RecyclerView = findRecyclerView(view)
            ?: throw ClassCastException("RecyclerView was not found in the ViewGroup hierarchy and could not be cast.")

        val adapter = recyclerView.adapter

        var isAutoScrolled = false

        widget.items.bind(viewFactoryContext.lifecycleOwner) { units ->
            val list = units.orEmpty()

            if ((!isAutoScrolled || isAlwaysAutoScroll) && list.isNotEmpty()) {
                recyclerView.smoothScrollToPosition(
                    adapter!!.itemCount - 1
                )

                isAutoScrolled = true
            }
        }

        return bundle
    }

    private fun findRecyclerView(view: View): RecyclerView? {
        return when (view) {
            is RecyclerView -> view
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    val childView = view.getChildAt(i)

                    val rv = findRecyclerView(childView)
                    if (rv != null) return rv
                }

                null
            }
            else -> null
        }
    }
}