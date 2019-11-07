/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.widget.FrameLayout
import androidx.lifecycle.Observer
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.view.MarginedFrameLayout

actual var statefulWidgetViewFactory: VFC<StatefulWidget<*, *>> = { context: ViewFactoryContext,
                                                                    widget: StatefulWidget<*, *> ->
    val ctx = context.context
    val lifecycleOwner = context.lifecycleOwner
    val style = widget.style

    val root = MarginedFrameLayout(ctx).apply {
        applyStyle(style)
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

    root
}
