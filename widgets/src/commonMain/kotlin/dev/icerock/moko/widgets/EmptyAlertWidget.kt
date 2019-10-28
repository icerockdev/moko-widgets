package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.mvvm.livedata.LiveData

expect var emptyAlertWidgetViewFactory: VFC<EmptyAlertWidget>

class EmptyAlertWidget(
    private val factory: VFC<EmptyAlertWidget>,
    val message: LiveData<StringDesc>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
