/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.factory.SystemTabsViewFactory
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

class TabsWidget<WS : WidgetSize>(
    private val factory: ViewFactory<TabsWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    tabs: TabsBuilder.() -> Unit
) : Widget<WS>(), OptionalId<TabsWidget.Id> {

    val tabs: List<Tab> = TabsBuilder().apply(tabs).build()

    class TabsBuilder {
        private val tabsList = mutableListOf<Tab>()

        fun tab(
            title: LiveData<StringDesc>,
            body: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
        ) {
            val tab = Tab(
                title = title,
                body = body
            )

            tabsList.add(tab)
        }

        fun build(): List<Tab> = tabsList
    }

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    class Tab(
        val title: LiveData<StringDesc>,
        val body: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
    )

    interface Id : Theme.Id<TabsWidget<out WidgetSize>>
    interface Category : Theme.Category<TabsWidget<out WidgetSize>>

    object DefaultCategory : Category
}

fun <WS : WidgetSize> Theme.tabs(
    category: TabsWidget.Category? = null,
    size: WS,
    id: TabsWidget.Id? = null,
    tabs: TabsWidget.TabsBuilder.() -> Unit
) = TabsWidget(
    factory = this.factory.get(
        id = id,
        category = category,
        defaultCategory = TabsWidget.DefaultCategory,
        fallback = { SystemTabsViewFactory() }
    ),
    size = size,
    id = id,
    tabs = tabs
)
