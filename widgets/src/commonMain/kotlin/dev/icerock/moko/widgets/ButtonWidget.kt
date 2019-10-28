package com.icerockdev.mpp.widgets

import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.TextStyle
import com.icerockdev.mpp.widgets.style.view.WidgetSize

expect var buttonWidgetViewFactory: VFC<ButtonWidget>

class ButtonWidget(
    private val _factory: VFC<ButtonWidget> = buttonWidgetViewFactory,
    val text: Source<StringDesc>,
    val enabled: Source<Boolean>? = null,
    val style: ButtonStyle = ButtonStyle(),
    val onTap: () -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View = _factory(viewFactoryContext, this)

    data class ButtonStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val isAllCaps: Boolean = false,
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Margined
}

fun Widget.button(
    text: StringDesc,
    onTap: () -> Unit
): ButtonWidget {
    return ButtonWidget(
        text = Source.Static(text),
        onTap = onTap
    )
}

fun Widget.button(
    textLiveData: LiveData<StringDesc>,
    onTap: () -> Unit
): ButtonWidget {
    return ButtonWidget(
        text = Source.Dynamic(textLiveData),
        onTap = onTap
    )
}

fun Widget.button(
    textLiveData: LiveData<StringDesc>,
    enabledLiveData: LiveData<Boolean>,
    onTap: () -> Unit
): ButtonWidget {
    return ButtonWidget(
        text = Source.Dynamic(textLiveData),
        enabled = Source.Dynamic(enabledLiveData),
        onTap = onTap
    )
}
