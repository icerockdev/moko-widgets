package com.icerockdev.mpp.widgets

import android.graphics.Rect
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.icerockdev.mpp.widgets.databinding.WidgetGridListBinding
import com.icerockdev.mpp.widgets.style.ext.setDpPadding

actual var gridListWidgetViewFactory: VFC<GridListWidget> = { context: ViewFactoryContext,
                                                              widget: GridListWidget ->
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(context.context)
    val binding: WidgetGridListBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_grid_list, parent, false)

    class CardDecorator : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            parent.adapter?.run {
                val position = parent.getChildAdapterPosition(view)
                if (position < itemCount - OFFSET_ITEMS) return
                val density = context.context.resources.displayMetrics.density

                outRect.bottom = (density * BOTTOM_PADDING).toInt()
            }

        }
    }

    binding.widget = widget

    binding.apply {
        widget.style.paddingValues.apply {
            root.setDpPadding(
                context.context.resources,
                paddingStart = start,
                paddingEnd = end,
                paddingBottom = bottom,
                paddingTop = top
            )
        }

        recyclerView.layoutManager =
            StaggeredGridLayoutManager(widget.style.spanCount, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(CardDecorator())
    }

    binding.lifecycleOwner = context.lifecycleOwner

    val onReachEnd = widget.onReachEnd
    val sourceLiveData = widget.items

    bindList(
        onReachEnd = onReachEnd,
        liveData = sourceLiveData
    ) { binding.list = it.ld() }

    binding.root
}

private const val OFFSET_ITEMS = 2
private const val BOTTOM_PADDING = 24
