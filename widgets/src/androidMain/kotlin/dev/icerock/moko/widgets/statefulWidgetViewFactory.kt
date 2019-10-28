package dev.icerock.moko.widgets

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.buildBackground

actual var statefulWidgetViewFactory: VFC<StatefulWidget<*, *>> = { context: ViewFactoryContext,
                                                                    widget: StatefulWidget<*, *> ->
    val ctx = context.context
    val lifecycleOwner = context.lifecycleOwner
    val style = widget.style

    val root = FrameLayout(ctx).apply {
        background = style.background.buildBackground(ctx)
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
