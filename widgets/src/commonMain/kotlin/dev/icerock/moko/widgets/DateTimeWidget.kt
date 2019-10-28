package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.widgets.*

expect var dateTimeWidgetViewFactory: VFC<DateTimeWidget>

class DateTimeWidget(
    private val _factory: VFC<DateTimeWidget> = dateTimeWidgetViewFactory,
    val orientation: FormWidget.Group.Orientation
) : Widget() {

    val items: MutableList<Widget> = mutableListOf()

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    fun add(widget: Widget) {
        items.add(widget)
    }
}

fun FormWidget.dateTimeField(
    orientation: FormWidget.Group.Orientation = FormWidget.Group.Orientation.HORIZONTAL,
    configure: DateTimeWidget.() -> Unit
) {
    val widget = DateTimeWidget(orientation = orientation)
    widget.configure()
    add(widget)
}
