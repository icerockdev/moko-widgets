package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.widgets.*
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.TextStyle
import com.icerockdev.mpp.widgets.style.view.WidgetSize

expect var dateWidgetViewFactory: VFC<DateWidget>

class DateWidget(
    private val _factory: VFC<DateWidget> = dateWidgetViewFactory,
    val label: Source<StringDesc>,
    val field: FormField<Date?, StringDesc>,
    val style: DateFieldStyle = DateFieldStyle()
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property underLineColor color of the underline
     * @property margins @see <com.icerockdev.mpp.widget.style.view.Margined>
     * @property background widget's background, can be null if none required
     */
    data class DateFieldStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val errorTextStyle: TextStyle = labelTextStyle,
        val underLineColor: Int = 0xFF000000.toInt(),
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Margined
}

sealed class Date(
    val year: Int,
    val month: Int,
    val day: Int
) {
    override fun toString(): String {
        return "$year-$month-$day"
    }

    object Empty : Date(0, 0, 0)

    object Incomplete : Date(0, 0, 0)

    class Filled(year: Int, month: Int, day: Int) : Date(year, month, day)
}

fun DateTimeWidget.dateField(
    label: StringDesc,
    field: FormField<Date?, StringDesc>
) {
    val widget = DateWidget(
        label = Source.Static(label),
        field = field
    )
    add(widget)
}

fun FormWidget.dateField(
    label: StringDesc,
    field: FormField<Date?, StringDesc>,
    style: DateWidget.DateFieldStyle = DateWidget.DateFieldStyle()
) {
    val widget = DateWidget(
        label = Source.Static(label),
        field = field,
        style = style
    )
    add(widget)
}
