/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.DrawableResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
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
        by WidgetScope.readProperty(FlatAlertWidget.FactoryKey, ::flatAlertWidgetViewFactory)

var WidgetScope.Builder.flatAlertFactory: VFC<FlatAlertWidget>
        by WidgetScope.readWriteProperty(FlatAlertWidget.FactoryKey, WidgetScope::flatAlertFactory)

val WidgetScope.flatAlertStyle: FlatAlertWidget.Style
        by WidgetScope.readProperty(FlatAlertWidget.StyleKey) { FlatAlertWidget.Style() }

var WidgetScope.Builder.flatAlertStyle: FlatAlertWidget.Style
        by WidgetScope.readWriteProperty(FlatAlertWidget.StyleKey, WidgetScope::flatAlertStyle)

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
