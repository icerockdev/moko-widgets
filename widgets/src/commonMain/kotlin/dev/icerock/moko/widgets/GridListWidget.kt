package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.binderadapter.generate.BindingClass
import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.widgets.style.view.PaddingValues

expect var gridListWidgetViewFactory: VFC<GridListWidget>

class GridListWidget(
    private val factory: VFC<GridListWidget>,
    val items: LiveData<List<BindingClass>>,
    val style: Style,
    val onReachEnd: (() -> Unit)?,
    val onRefresh: (() -> Unit)?
) : Widget() {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    data class Style(
        val spanCount: Int = 2,
        val orientation: Orientation = Orientation.VERTICAL,
        val paddingValues: PaddingValues = PaddingValues()
    )
}
