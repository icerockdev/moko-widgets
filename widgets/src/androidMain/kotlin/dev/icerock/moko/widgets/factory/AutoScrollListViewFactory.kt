package dev.icerock.moko.widgets.factory

import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

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
        val recyclerView = bundle.view as RecyclerView
        val adapter = recyclerView.adapter as UnitsRecyclerViewAdapter

        var isAutoScrolled = false

        widget.items.bind(viewFactoryContext.lifecycleOwner) { units ->
            val list = units.orEmpty()

            if ((!isAutoScrolled || isAlwaysAutoScroll) && list.isNotEmpty()) {
                recyclerView.smoothScrollToPosition(
                    adapter.itemCount - 1
                )

                isAutoScrolled = true
            }
        }

        return bundle
    }
}