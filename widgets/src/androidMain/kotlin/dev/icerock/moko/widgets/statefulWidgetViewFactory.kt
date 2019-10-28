package com.icerockdev.mpp.widgets

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.mvvm.State

actual var statefulWidgetViewFactory: VFC<StatefulWidget<*, *>> = { context: ViewFactoryContext,
                                                                    widget: StatefulWidget<*, *> ->
    val ctx = context.context
    val lifecycleOwner = context.lifecycleOwner

    val root = FrameLayout(ctx).apply {
        setBackgroundColor(resources.getColor(android.R.color.white, null))
    }

    val factoryContext = ViewFactoryContext(ctx, lifecycleOwner, root)

    val dataView = widget.dataWidget.buildView(factoryContext)
    val loadingView = widget.loadingWidget.buildView(factoryContext)
    val emptyView = widget.emptyWidget.buildView(factoryContext)
    val errorView = widget.errorWidget.buildView(factoryContext)
    val views = listOf(dataView, loadingView, emptyView, errorView)

    views.forEach { view ->
        view.visibility = View.GONE
        root.addView(view)
    }

    widget.stateLiveData.ld().observe(context.lifecycleOwner, Observer { state ->
        val currentView = when (state) {
            is State.Empty -> emptyView
            is State.Loading -> loadingView
            is State.Data -> dataView
            is State.Error -> errorView
            else -> return@Observer
        }

        views.forEach { it.visibility = View.GONE }
        currentView.visibility = View.VISIBLE
    })

    root.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    root
}
