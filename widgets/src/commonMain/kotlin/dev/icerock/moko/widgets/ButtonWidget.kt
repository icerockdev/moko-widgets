package dev.icerock.moko.widgets

import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.*
import dev.icerock.moko.widgets.style.background.Background

expect var buttonWidgetViewFactory: VFC<ButtonWidget>

class ButtonWidget(
    private val factory: VFC<ButtonWidget> = buttonWidgetViewFactory,
    val text: LiveData<StringDesc>,
    val enabled: LiveData<Boolean>? = null,
    val style: Style = Style(),
    val onTap: () -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    data class Style(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val isAllCaps: Boolean = false,
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Margined

    internal object FactoryKey : WidgetScope.Key
    internal object StyleKey : WidgetScope.Key
}

val WidgetScope.buttonFactory: VFC<ButtonWidget>
    get() {
        val factory = properties[ButtonWidget.FactoryKey] as? VFC<ButtonWidget>
        return factory ?: buttonWidgetViewFactory
    }

val WidgetScope.buttonStyle: ButtonWidget.Style
    get() {
        val style = properties[ButtonWidget.StyleKey] as? ButtonWidget.Style
        return style ?: ButtonWidget.Style()
    }

fun WidgetScope.button(
    factory: VFC<ButtonWidget> = this.buttonFactory,
    style: ButtonWidget.Style = this.buttonStyle,
    text: LiveData<StringDesc>,
    enabled: LiveData<Boolean>? = null,
    onTap: () -> Unit
) = ButtonWidget(
    factory = factory,
    style = style,
    text = text,
    enabled = enabled,
    onTap = onTap
)
