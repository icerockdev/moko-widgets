package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.mvvm.livedata.MutableLiveData
import com.icerockdev.mpp.widgets.*
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.*

expect var switchWidgetViewFactory: VFC<SwitchWidget>

class SwitchWidget(
    private val _factory: VFC<SwitchWidget> = switchWidgetViewFactory,
    val style: SwitchStyle,
    val label: Source<StringDesc>,
    val state: MutableLiveData<Boolean>
) : Widget() {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property labelTextStyle floating label text style
     * @property paddings @see com.icerockdev.mpp.widget.style.view.Padded
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property background widget's background, might be null if not required
     * @property switchColor switch background, might be null if default
     */

    //TODO: Add icon

    data class SwitchStyle(
        val size: WidgetSize = WidgetSize(),
        val labelTextStyle: TextStyle = TextStyle(size = 14),
        override val paddings: PaddingValues = PaddingValues(),
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null,
        val switchColor: ColorStyle? = null
    ) : Padded, Margined
}

fun FormWidget.switchField(
    label: StringDesc,
    state: MutableLiveData<Boolean>,
    style: SwitchWidget.SwitchStyle
) {
    val widget = SwitchWidget(
        label = Source.Static(label),
        state = state,
        style = style
    )
    add(widget)
}
