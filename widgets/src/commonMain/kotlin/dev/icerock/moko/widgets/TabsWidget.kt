package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.mvvm.livedata.LiveData

expect var tabsWidgetViewFactory: VFC<TabsWidget>

class TabsWidget(
    private val factory: VFC<TabsWidget>,
    val tabs: List<TabWidget>
) : Widget() {

    constructor(
        factory: VFC<TabsWidget>,
        builder: Builder.() -> Unit
    ) : this(
        factory = factory,
        tabs = Builder().apply(builder).build()
    )

    class TabWidget(
        val title: Source<StringDesc>,
        val body: Widget
    )

    class Builder {
        private val _tabs: MutableList<TabWidget> = mutableListOf()

        fun tab(title: StringDesc, factory: () -> Widget) {
            _tabs.add(
                TabWidget(
                    title = Source.Static(title),
                    body = factory()
                )
            )
        }

        fun tab(title: LiveData<StringDesc>, factory: () -> Widget) {
            _tabs.add(
                TabWidget(
                    title = Source.Dynamic(title),
                    body = factory()
                )
            )
        }

        fun build() = _tabs
    }

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
