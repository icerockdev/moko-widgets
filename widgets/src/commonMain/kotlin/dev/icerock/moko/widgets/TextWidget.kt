package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.mvvm.livedata.LiveData

expect var textWidgetViewFactory: VFC<TextWidget>

class TextWidget(
    private val _factory: VFC<TextWidget> = textWidgetViewFactory,
    val text: Source<StringDesc>,
    val style: Style
) : Widget() {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        _factory(viewFactoryContext, this)

    enum class FontStyle {
        BODY,
        HEADER1,
        HEADER2,
        HEADER3,
        HEADER4
    }

    data class Style(
        val fontStyle: FontStyle = FontStyle.BODY
    )
}

@Suppress("unused")
fun Widget.text(
    text: StringDesc,
    style: TextWidget.Style = TextWidget.Style()
): TextWidget {
    return TextWidget(
        text = Source.Static(text),
        style = style
    )
}

@Suppress("unused")
fun Widget.text(
    textLiveData: LiveData<StringDesc>,
    style: TextWidget.Style = TextWidget.Style()
): TextWidget {
    return TextWidget(
        text = Source.Dynamic(textLiveData),
        style = style
    )
}
