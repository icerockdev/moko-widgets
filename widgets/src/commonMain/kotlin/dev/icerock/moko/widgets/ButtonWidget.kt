/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var buttonWidgetViewFactory: VFC<ButtonWidget>

class ButtonWidget(
    private val factory: VFC<ButtonWidget>,
    val style: Style,
    val id: Id?,
    val text: LiveData<StringDesc>,
    val enabled: LiveData<Boolean>?,
    val onTap: () -> Unit
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    data class Style(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val isAllCaps: Boolean? = null,
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Margined

    internal object FactoryKey : WidgetScope.Key<VFC<ButtonWidget>>
    internal object StyleKey : WidgetScope.Key<Style>

    interface Id : WidgetScope.Id
}

val WidgetScope.buttonFactory: VFC<ButtonWidget>
        by WidgetScope.readProperty(ButtonWidget.FactoryKey, ::buttonWidgetViewFactory)

var WidgetScope.Builder.buttonFactory: VFC<ButtonWidget>
        by WidgetScope.readWriteProperty(ButtonWidget.FactoryKey, WidgetScope::buttonFactory)

val WidgetScope.buttonStyle: ButtonWidget.Style
        by WidgetScope.readProperty(ButtonWidget.StyleKey) { ButtonWidget.Style() }

var WidgetScope.Builder.buttonStyle: ButtonWidget.Style
        by WidgetScope.readWriteProperty(ButtonWidget.StyleKey, WidgetScope::buttonStyle)

fun WidgetScope.getButtonStyle(id: ButtonWidget.Id): ButtonWidget.Style {
    return getIdProperty(id, ButtonWidget.StyleKey, ::buttonStyle)
}

fun WidgetScope.Builder.setButtonStyle(style: ButtonWidget.Style, vararg ids: ButtonWidget.Id) {
    ids.forEach { setIdProperty(it, ButtonWidget.StyleKey, style) }
}

fun WidgetScope.button(
    factory: VFC<ButtonWidget> = this.buttonFactory,
    style: ButtonWidget.Style,
    id: ButtonWidget.Id? = null,
    text: LiveData<StringDesc>,
    enabled: LiveData<Boolean>? = null,
    onTap: () -> Unit
) = ButtonWidget(
    factory = factory,
    style = style,
    id = id,
    text = text,
    enabled = enabled,
    onTap = onTap
)

fun WidgetScope.button(
    factory: VFC<ButtonWidget> = this.buttonFactory,
    id: ButtonWidget.Id? = null,
    text: LiveData<StringDesc>,
    enabled: LiveData<Boolean>? = null,
    onTap: () -> Unit
) = ButtonWidget(
    factory = factory,
    style = id?.let { this.getButtonStyle(it) } ?: this.buttonStyle,
    id = id,
    text = text,
    enabled = enabled,
    onTap = onTap
)

fun WidgetScope.button(
    factory: VFC<ButtonWidget> = this.buttonFactory,
    style: (ButtonWidget.Style) -> ButtonWidget.Style,
    id: ButtonWidget.Id? = null,
    text: LiveData<StringDesc>,
    enabled: LiveData<Boolean>? = null,
    onTap: () -> Unit
) = ButtonWidget(
    factory = factory,
    style = style(id?.let { this.getButtonStyle(it) } ?: this.buttonStyle),
    id = id,
    text = text,
    enabled = enabled,
    onTap = onTap
)
