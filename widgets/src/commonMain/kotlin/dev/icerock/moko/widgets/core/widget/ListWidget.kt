/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.widget

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.RequireId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.factory.SystemListViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize

@WidgetDef(SystemListViewFactory::class)
class ListWidget<WS : WidgetSize>(
    private val factory: ViewFactory<ListWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id,
    val items: LiveData<List<TableUnitItem>>,
    val onReachEnd: (() -> Unit)?,
    val onRefresh: ((completion: () -> Unit) -> Unit)?
) : Widget<WS>(), RequireId<ListWidget.Id> {

    internal var lastScrollView: ScrollListView? = null

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    interface Id : Theme.Id<ListWidget<out WidgetSize>>
    interface Category : Theme.Category<ListWidget<out WidgetSize>>

    object DefaultCategory : Category

    fun scrollToPosition(index: Int) {
        lastScrollView?.scrollToPosition(index)
    }
}

interface ScrollListView {
    fun scrollToPosition(index: Int)
}
