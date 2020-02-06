package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class AutoScrollListViewFactory actual constructor(
    private val listViewFactory: ViewFactory<ListWidget<out WidgetSize>>,
    private val isAlwaysAutoScroll: Boolean
) : ViewFactory<ListWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ListWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = listViewFactory.build(widget, size, viewFactoryContext)

        // TODO

        return bundle
    }
}