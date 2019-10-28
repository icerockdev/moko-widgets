package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.TextStyle
import com.icerockdev.mpp.widgets.style.view.WidgetSize
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var buttonWidgetViewFactory: VFC<ButtonWidget>

class ButtonWidget(
    private val factory: VFC<ButtonWidget> = buttonWidgetViewFactory,
    val text: LiveData<StringDesc>,
    val enabled: LiveData<Boolean>? = null,
    val style: ButtonStyle = ButtonStyle(),
    val onTap: () -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View = factory(viewFactoryContext, this)

    data class ButtonStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val isAllCaps: Boolean = false,
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Margined
}
