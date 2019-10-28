package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.mvvm.livedata.LiveData

expect var flatAlertWidgetViewFactory: VFC<FlatAlertWidget>

class FlatAlertWidget(
    private val factory: VFC<FlatAlertWidget>,
    val title: LiveData<StringDesc?>,
    val message: LiveData<StringDesc?>,
    val buttonText: LiveData<StringDesc?>,
    val onTap: () -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
