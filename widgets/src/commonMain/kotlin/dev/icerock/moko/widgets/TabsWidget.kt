package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.core.*
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
    get() {
        val factory = properties[TabsWidget.FactoryKey] as? VFC<TabsWidget>
        return (factory ?: tabsWidgetViewFactory)
    }

var WidgetScope.Builder.tabsFactory: VFC<TabsWidget>
    get() {
        return scope.tabsFactory
    }
    set(value) {
        scope.properties[TabsWidget.FactoryKey] = value
    }

val WidgetScope.tabsStyle: TabsWidget.Style
    get() {
        val style = properties[TabsWidget.StyleKey] as? TabsWidget.Style
        return style ?: TabsWidget.Style()
    }

var WidgetScope.Builder.tabsStyle: TabsWidget.Style
    get() {
        return scope.tabsStyle
    }
    set(value) {
        scope.properties[TabsWidget.StyleKey] = value
    }

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
