package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.binderadapter.generate.BindingClass
import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.widgets.style.view.PaddingValues

expect var linearListWidgetViewFactory: VFC<LinearListWidget>

class LinearListWidget(
    private val _factory: VFC<LinearListWidget> = linearListWidgetViewFactory,
    val source: Source<List<BindingClass>>,
    val style: Style,
    val onReachEnd: (() -> Unit)?,
    val onRefresh: (() -> Unit)?
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View = _factory(viewFactoryContext, this)

    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    data class Style(
        val orientation: Orientation = Orientation.VERTICAL,
        val reversed: Boolean = false,
        val paddingValues: PaddingValues = PaddingValues()
    )
}

fun Widget.linearList(
    list: List<BindingClass>,
    style: LinearListWidget.Style = LinearListWidget.Style(),
    onReachEnd: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null
): LinearListWidget {
    return LinearListWidget(
        source = Source.Static(list),
        style = style,
        onReachEnd = onReachEnd,
        onRefresh = onRefresh
    )
}

fun Widget.linearList(
    listLiveData: LiveData<List<BindingClass>>,
    style: LinearListWidget.Style = LinearListWidget.Style(),
    onReachEnd: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null
): LinearListWidget {
    return LinearListWidget(
        source = Source.Dynamic(listLiveData),
        style = style,
        onReachEnd = onReachEnd,
        onRefresh = onRefresh
    )
}
