package dev.icerock.moko.widgets.old

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var timeWidgetViewFactory: VFC<TimeWidget>

class TimeWidget(
    private val factory: VFC<TimeWidget> = timeWidgetViewFactory,
    val label: LiveData<StringDesc>,
    val field: FormField<Time?, StringDesc>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
