/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var gridListWidgetViewFactory: VFC<GridListWidget> = { context: ViewFactoryContext,
                                                              widget: GridListWidget ->
    TODO()
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(context.context)
//    val binding: WidgetGridListBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_grid_list, parent, false)
//
//    class CardDecorator : RecyclerView.ItemDecoration() {
//        override fun getItemOffsets(
//            outRect: Rect,
//            view: View,
//            parent: RecyclerView,
//            state: RecyclerView.State
//        ) {
//            super.getItemOffsets(outRect, view, parent, state)
//            parent.adapter?.run {
//                val position = parent.getChildAdapterPosition(view)
//                if (position < itemCount - OFFSET_ITEMS) return
//                val density = context.context.resources.displayMetrics.density
//
//                outRect.bottom = (density * BOTTOM_PADDING).toInt()
//            }
//
//        }
//    }
//
//    binding.widget = widget
//
//    binding.apply {
//        widget.style.paddingValues.apply {
//            root.setDpPadding(
//                context.context.resources,
//                paddingStart = start,
//                paddingEnd = end,
//                paddingBottom = bottom,
//                paddingTop = top
//            )
//        }
//
//        recyclerView.layoutManager =
//            StaggeredGridLayoutManager(widget.style.spanCount, StaggeredGridLayoutManager.VERTICAL)
//        recyclerView.addItemDecoration(CardDecorator())
//    }
//
//    binding.lifecycleOwner = context.lifecycleOwner
//
//    val onReachEnd = widget.onReachEnd
//    val sourceLiveData = widget.items
//
//    bindList(
//        onReachEnd = onReachEnd,
//        liveData = sourceLiveData
//    ) { binding.list = it.ld() }
//
//    binding.root
}

private const val OFFSET_ITEMS = 2
private const val BOTTOM_PADDING = 24
