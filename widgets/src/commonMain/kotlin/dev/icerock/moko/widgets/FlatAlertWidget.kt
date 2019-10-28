package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.DrawableResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.*
import dev.icerock.moko.widgets.style.background.Background

expect var flatAlertWidgetViewFactory: VFC<FlatAlertWidget>

class FlatAlertWidget(
    private val factory: VFC<FlatAlertWidget>,
    val style: Style,
    val title: LiveData<StringDesc?>?,
    val message: LiveData<StringDesc?>,
    val drawable: LiveData<DrawableResource?>?,
    val buttonText: LiveData<StringDesc?>?,
    val onTap: (() -> Unit)?
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    data class Style(
        val background: Background = Background()
    )

    internal object FactoryKey : WidgetScope.Key
    internal object StyleKey : WidgetScope.Key
}

val WidgetScope.flatAlertFactory: VFC<FlatAlertWidget>
    get() {
        val factory = properties[FlatAlertWidget.FactoryKey] as? VFC<FlatAlertWidget>
        return (factory ?: flatAlertWidgetViewFactory)
    }

var WidgetScope.Builder.flatAlertFactory: VFC<FlatAlertWidget>
    get() {
        return scope.flatAlertFactory
    }
    set(value) {
        scope.properties[FlatAlertWidget.FactoryKey] = value
    }

val WidgetScope.flatAlertStyle: FlatAlertWidget.Style
    get() {
        val style = properties[FlatAlertWidget.StyleKey] as? FlatAlertWidget.Style
        return style ?: FlatAlertWidget.Style()
    }

var WidgetScope.Builder.flatAlertStyle: FlatAlertWidget.Style
    get() {
        return scope.flatAlertStyle
    }
    set(value) {
        scope.properties[FlatAlertWidget.StyleKey] = value
    }

fun WidgetScope.flatAlert(
    factory: VFC<FlatAlertWidget> = this.flatAlertFactory,
    style: FlatAlertWidget.Style = this.flatAlertStyle,
    title: LiveData<StringDesc?>? = null,
    message: LiveData<StringDesc?>,
    drawable: LiveData<DrawableResource?>? = null,
    buttonText: LiveData<StringDesc?>? = null,
    onTap: (() -> Unit)? = null
): FlatAlertWidget {
    return FlatAlertWidget(
        factory = factory,
        style = style,
        title = title,
        message = message,
        drawable = drawable,
        buttonText = buttonText,
        onTap = onTap
    )
}
