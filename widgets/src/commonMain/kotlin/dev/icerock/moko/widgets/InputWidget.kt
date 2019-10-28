package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.mvvm.livedata.MutableLiveData
import com.icerockdev.mpp.widgets.*
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.input.InputType
import com.icerockdev.mpp.widgets.style.view.*

expect var inputWidgetViewFactory: VFC<InputWidget>

class InputWidget(
    private val _factory: VFC<InputWidget> = inputWidgetViewFactory,
    val label: Source<StringDesc>,
    val field: FormField<String, StringDesc>,
    val enabled: Source<Boolean>? = null,
    val style: InputStyle,
    val inputType: InputType = InputType.PLAIN_TEXT,
    val maxLines: Source<Int?>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property errorTextStyle error message text style
     * @property counterEnabled true, if need to show symbol counter
     * @property underLineColor color of the underline
     * @property paddings @see com.icerockdev.mpp.widget.style.view.Padded
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property background widget's background, might be null if not required
     */
    data class InputStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(size = 12),
        val errorTextStyle: TextStyle = labelTextStyle,
        val counterEnabled: Boolean = false,
        val underLineColor: Int = 0xFFFFFFFF.toInt(),
        override val paddings: PaddingValues = PaddingValues(),
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Padded, Margined
}

fun FormWidget.inputField(
    label: StringDesc,
    field: FormField<String, StringDesc>,
    enabledLiveData: LiveData<Boolean>? = null,
    maxLines: Int? = 1,
    inputType: InputType = InputType.PLAIN_TEXT,
    style: InputWidget.InputStyle
) {
    val enabled =
        (enabledLiveData ?: MutableLiveData(true)).let { Source.Dynamic(it) }

    val widget = InputWidget(
        label = Source.Static(label),
        field = field,
        enabled = enabled,
        inputType = inputType,
        maxLines = Source.Static(maxLines),
        style = style
    )
    add(widget)
}
