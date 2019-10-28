package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.widgets.*

expect var timeWidgetViewFactory: VFC<TimeWidget>

class TimeWidget(
    private val _factory: VFC<TimeWidget> = timeWidgetViewFactory,
    val label: Source<StringDesc>,
    val field: FormField<Time?, StringDesc>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)
}

data class Time(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) {
    override fun toString(): String {
        return "$hours:${minutes.formatTime()}"
    }

    private fun Int.formatTime() = this.toString().padStart(2, '0')
}

fun DateTimeWidget.timeField(
    label: StringDesc,
    field: FormField<Time?, StringDesc>
) {
    val widget = TimeWidget(
        label = Source.Static(label),
        field = field
    )
    add(widget)
}