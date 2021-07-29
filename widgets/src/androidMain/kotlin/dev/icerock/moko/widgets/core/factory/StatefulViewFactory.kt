/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.view.View
import android.widget.FrameLayout
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.widgets.core.widget.StatefulWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.ext.applyMargin
import dev.icerock.moko.widgets.core.style.ext.toPlatformSize
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bindNotNull

actual class StatefulViewFactory actual constructor(
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val background: Background<out Fill>?
) : ViewFactory<StatefulWidget<out WidgetSize, *, *>> {

    override fun <WS : WidgetSize> build(
        widget: StatefulWidget<out WidgetSize, *, *>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val root = FrameLayout(context).apply {
            applyBackgroundIfNeeded(this@StatefulViewFactory.background)
            applyPaddingIfNeeded(padding)
        }

        val factoryContext = ViewFactoryContext(context, lifecycleOwner, root)

        val dataView = widget.dataWidget.buildView(factoryContext)
        val loadingView = widget.loadingWidget.buildView(factoryContext)
        val emptyView = widget.emptyWidget.buildView(factoryContext)
        val errorView = widget.errorWidget.buildView(factoryContext)
        val views = listOf(dataView, loadingView, emptyView, errorView)

        views.forEach { viewBundle ->
            val view = viewBundle.view
            view.visibility = View.GONE
            root.addView(view, FrameLayout.LayoutParams(
                viewBundle.size.width.toPlatformSize(dm),
                viewBundle.size.height.toPlatformSize(dm)
            ).apply {
                viewBundle.margins?.let { applyMargin(dm, it) }
            })
        }

        widget.state.bindNotNull(lifecycleOwner) { state ->
            val currentView = when (state) {
                is ResourceState.Empty -> emptyView
                is ResourceState.Loading -> loadingView
                is ResourceState.Success -> dataView
                is ResourceState.Failed -> errorView
            }

            views.asSequence()
                .filter { it.view.visibility != View.GONE && it.view != currentView.view }
                .forEach { it.view.visibility = View.GONE }

            if (currentView.view.visibility != View.VISIBLE) {
                currentView.view.visibility = View.VISIBLE
            }
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }
}
