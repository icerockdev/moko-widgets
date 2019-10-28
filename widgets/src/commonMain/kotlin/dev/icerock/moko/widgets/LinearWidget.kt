package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.*
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var linearWidgetFactory: VFC<LinearWidget>

class LinearWidget(
    private val factory: VFC<LinearWidget>,
    val style: Style,
    val childs: List<Widget>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    data class Style(
        val background: Background = Background(),
        val orientation: Orientation = Orientation.VERTICAL,
        val size: WidgetSize = WidgetSize(
            width = SizeSpec.AS_PARENT,
            height = SizeSpec.AS_PARENT
        )
    )

    internal object FactoryKey : WidgetScope.Key
    internal object StyleKey : WidgetScope.Key
}

val WidgetScope.linearFactory: VFC<LinearWidget>
    get() {
        val factory = properties[LinearWidget.FactoryKey] as? VFC<LinearWidget>
        return factory ?: linearWidgetFactory
    }

val WidgetScope.linearStyle: LinearWidget.Style
    get() {
        val style = properties[LinearWidget.StyleKey] as? LinearWidget.Style
        return style ?: LinearWidget.Style()
    }

fun WidgetScope.linear(
    factory: VFC<LinearWidget> = this.linearFactory,
    style: LinearWidget.Style = this.linearStyle,
    childs: List<Widget>
) = LinearWidget(
    factory = factory,
    style = style,
    childs = childs
)
