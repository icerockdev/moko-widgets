/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background

expect var tabsWidgetViewFactory: VFC<TabsWidget>

class TabsWidget(
    private val factory: VFC<TabsWidget>,
    val style: Style,
    val tabs: List<TabWidget>
) : Widget() {

    class TabWidget(
        val title: LiveData<StringDesc>,
        val body: Widget
    )

    class Builder() {
        private val tabs: MutableList<TabWidget> = mutableListOf()

        fun tab(
            title: LiveData<StringDesc>,
            body: Widget
        ) {
            TabWidget(
                title = title,
                body = body
            ).also { tabs.add(it) }
        }

        fun build(
            factory: VFC<TabsWidget>,
            style: Style
        ): TabsWidget = TabsWidget(
            factory = factory,
            style = style,
            tabs = tabs
        )
    }

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    data class Style(
        val background: Background = Background()
    )

    internal object FactoryKey : WidgetScope.Key
    internal object StyleKey : WidgetScope.Key
}

val WidgetScope.tabsFactory: VFC<TabsWidget>
        by WidgetScope.readProperty(TabsWidget.FactoryKey, ::tabsWidgetViewFactory)

var WidgetScope.Builder.tabsFactory: VFC<TabsWidget>
        by WidgetScope.readWriteProperty(TabsWidget.FactoryKey, WidgetScope::tabsFactory)

val WidgetScope.tabsStyle: TabsWidget.Style
        by WidgetScope.readProperty(TabsWidget.StyleKey) { TabsWidget.Style() }

var WidgetScope.Builder.tabsStyle: TabsWidget.Style
        by WidgetScope.readWriteProperty(TabsWidget.StyleKey, WidgetScope::tabsStyle)

fun WidgetScope.tabs(
    factory: VFC<TabsWidget> = this.tabsFactory,
    style: TabsWidget.Style = this.tabsStyle,
    builder: TabsWidget.Builder.() -> Unit
): TabsWidget {
    return TabsWidget.Builder()
        .apply(builder)
        .build(
            factory = factory,
            style = style
        )
}
