package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.widgets.*
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.TextStyle
import com.icerockdev.mpp.widgets.style.view.WidgetSize

expect var headerWidgetViewFactory: VFC<HeaderWidget>

class HeaderWidget(
    private val _factory: VFC<HeaderWidget> = headerWidgetViewFactory,
    val text: Source<StringDesc>,
    val style: HeaderStyle = HeaderStyle()
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style header's text
     * @property underLineColor color of the underline
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property background widget's background, might be null if not required
     */
    data class HeaderStyle(
            val size: WidgetSize = WidgetSize(),
            val textStyle: TextStyle = TextStyle(),
            override val margins: MarginValues = MarginValues(),
            val underlineColor: Int = 0xFF000000.toInt(),
            val background: Background? = null
    ) : Margined
}

fun FormWidget.header(
    text: StringDesc,
    style: HeaderWidget.HeaderStyle = HeaderWidget.HeaderStyle()
) {
    val widget = HeaderWidget(
        text = Source.Static(text),
        style = style
    )
    add(widget)
}
