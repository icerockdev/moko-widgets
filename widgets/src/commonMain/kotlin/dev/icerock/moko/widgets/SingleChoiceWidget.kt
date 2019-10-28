package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.widgets.*
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.TextStyle
import com.icerockdev.mpp.widgets.style.view.WidgetSize

expect var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget>

class SingleChoiceWidget(
    private val _factory: VFC<SingleChoiceWidget> = singleChoiceWidgetViewFactory,
    val label: Source<StringDesc>,
    val cancelLabel: Source<StringDesc>,
    val values: LiveData<List<StringDesc>>,
    val field: FormField<Int, StringDesc>,
    val style: SingleChoiceWidgetStyle = SingleChoiceWidgetStyle()
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)
    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property underLineColor color of the underline
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property dropDownBackground widget's dropdown view background, might be null if not required
     */
    data class SingleChoiceWidgetStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val dropDownTextColor: Int = 0xFF000000.toInt(),
        val underlineColor: Int = 0xFF000000.toInt(),
        override val margins: MarginValues = MarginValues(),
        val dropDownBackground: Background? = null
    ) : Margined
}

fun FormWidget.singleChoiceField(
    label: StringDesc,
    cancelLabel: StringDesc,
    values: LiveData<List<StringDesc>>,
    field: FormField<Int, StringDesc>,
    style: SingleChoiceWidget.SingleChoiceWidgetStyle = SingleChoiceWidget.SingleChoiceWidgetStyle()
) {
    val widget = SingleChoiceWidget(
        label = Source.Static(label),
        cancelLabel = Source.Static(cancelLabel),
        values = values,
        field = field,
        style = style
    )
    add(widget)
}