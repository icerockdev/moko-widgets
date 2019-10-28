package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.mvvm.State
import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.mvvm.livedata.data
import com.icerockdev.mpp.mvvm.livedata.error

expect var statefulWidgetViewFactory: VFC<StatefulWidget<*, *>>

class StatefulWidget<T, E>(
    private val factory: VFC<StatefulWidget<*, *>>,
    val stateLiveData: LiveData<State<T, E>>,
    val emptyWidget: Widget,
    val loadingWidget: Widget,
    val dataWidget: Widget,
    val errorWidget: Widget
) : Widget() {

    constructor(
        factory: VFC<StatefulWidget<*, *>>,
        stateLiveData: LiveData<State<T, E>>,
        emptyWidget: () -> Widget,
        loadingWidget: () -> Widget,
        dataWidget: (data: LiveData<T?>) -> Widget,
        errorWidget: (error: LiveData<E?>) -> Widget
    ): this(
        factory = factory,
        stateLiveData = stateLiveData,
        emptyWidget = emptyWidget(),
        loadingWidget = loadingWidget(),
        dataWidget = dataWidget(stateLiveData.data()),
        errorWidget = errorWidget(stateLiveData.error())
    )

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
