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
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var textWidgetViewFactory: VFC<TextWidget>

class TextWidget(
    private val factory: VFC<TextWidget>,
    val text: LiveData<StringDesc>,
    val style: Style
) : Widget() {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    data class Style(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        override val padding: PaddingValues = PaddingValues(),
        override val margins: MarginValues = MarginValues()
    ) : Padded, Margined

    object FactoryKey : WidgetScope.Key
    object StyleKey : WidgetScope.Key
}

val WidgetScope.textFactory: VFC<TextWidget>
        by WidgetScope.readProperty(TextWidget.FactoryKey, ::textWidgetViewFactory)

var WidgetScope.Builder.textFactory: VFC<TextWidget>
        by WidgetScope.readWriteProperty(TextWidget.FactoryKey, WidgetScope::textFactory)

val WidgetScope.textStyle: TextWidget.Style
        by WidgetScope.readProperty(TextWidget.StyleKey) { TextWidget.Style() }

var WidgetScope.Builder.textStyle: TextWidget.Style
        by WidgetScope.readWriteProperty(TextWidget.StyleKey, WidgetScope::textStyle)

fun WidgetScope.text(
    factory: VFC<TextWidget> = this.textFactory,
    style: TextWidget.Style = this.textStyle,
    text: LiveData<StringDesc>
) = TextWidget(
    factory = factory,
    style = style,
    text = text
)
