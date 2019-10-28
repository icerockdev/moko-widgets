package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.widgets.*
import com.icerockdev.mpp.widgets.style.view.Padded
import com.icerockdev.mpp.widgets.style.view.PaddingValues
import com.icerockdev.mpp.widgets.style.view.SizeSpec
import com.icerockdev.mpp.widgets.style.view.WidgetSize

expect var formWidgetViewFactory: VFC<FormWidget>

class FormWidget(
    private val _factory: VFC<FormWidget> = formWidgetViewFactory,
    val style: FormStyle,
    val isLoading: LiveData<Boolean>
) : Widget() {

    val items: MutableList<Widget> = mutableListOf()

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    fun add(widget: Widget) {
        items.add(widget)
    }

    data class FormStyle(
        val size: WidgetSize = WidgetSize(SizeSpec.WRAP_CONTENT, SizeSpec.WRAP_CONTENT),
        val orientation: Group.Orientation = Group.Orientation.VERTICAL,
        val spacing: Float = 0.0F,
        override val paddings: PaddingValues = PaddingValues(0.0F)
    ) : Padded

    class Group {
        enum class Orientation {
            HORIZONTAL,
            VERTICAL
        }
    }
}

fun Widget.form(
    style: FormWidget.FormStyle = FormWidget.FormStyle(),
    isLoading: LiveData<Boolean>,
    configure: FormWidget.() -> Unit
): FormWidget {
    val widget = FormWidget(style = style, isLoading = isLoading)
    widget.configure()
    return widget
}

abstract class FormStyleSet {

    open val formStyle: FormWidget.FormStyle = FormWidget.FormStyle()

    open val buttonStyle: ButtonWidget.ButtonStyle = ButtonWidget.ButtonStyle()

    open val inputStyle: InputWidget.InputStyle = InputWidget.InputStyle()

    open val headerStyle: HeaderWidget.HeaderStyle = HeaderWidget.HeaderStyle()

    open val dateFieldStyle: DateWidget.DateFieldStyle = DateWidget.DateFieldStyle()

    open val singleChoiceStyle: SingleChoiceWidget.SingleChoiceWidgetStyle =
        SingleChoiceWidget.SingleChoiceWidgetStyle()

    open val switchStyle: SwitchWidget.SwitchStyle = SwitchWidget.SwitchStyle()
}