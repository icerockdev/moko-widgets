/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.input.InputType
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var inputWidgetViewFactory: VFC<InputWidget>

class InputWidget(
    private val factory: VFC<InputWidget>,
    val style: Style,
    val id: Id,
    val label: LiveData<StringDesc>,
    val field: FormField<String, StringDesc>,
    val enabled: LiveData<Boolean>?,
    val inputType: InputType,
    val maxLines: LiveData<Int?>?
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property errorTextStyle error message text style
     * @property underLineColor color of the underline
     * @property padding @see com.icerockdev.mpp.widget.style.view.Padded
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property background widget's background, might be null if not required
     */
    data class Style(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val errorTextStyle: TextStyle = TextStyle(),
        val underLineColor: Color? = null,
        override val padding: PaddingValues = PaddingValues(),
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Padded, Margined

    object FactoryKey : WidgetScope.Key
    object StyleKey : WidgetScope.Key

    interface Id : WidgetScope.Id
}

val WidgetScope.inputFactory: VFC<InputWidget>
        by WidgetScope.readProperty(InputWidget.FactoryKey, ::inputWidgetViewFactory)

var WidgetScope.Builder.inputFactory: VFC<InputWidget>
        by WidgetScope.readWriteProperty(InputWidget.FactoryKey, WidgetScope::inputFactory)

val WidgetScope.inputStyle: InputWidget.Style
        by WidgetScope.readProperty(InputWidget.StyleKey) { InputWidget.Style() }

var WidgetScope.Builder.inputStyle: InputWidget.Style
        by WidgetScope.readWriteProperty(InputWidget.StyleKey, WidgetScope::inputStyle)

fun WidgetScope.input(
    factory: VFC<InputWidget> = this.inputFactory,
    style: InputWidget.Style = this.inputStyle,
    id: InputWidget.Id,
    label: LiveData<StringDesc>,
    field: FormField<String, StringDesc>,
    enabled: LiveData<Boolean>? = null,
    inputType: InputType = InputType.PLAIN_TEXT,
    maxLines: LiveData<Int?>? = null
) = InputWidget(
    factory = factory,
    style = style,
    id = id,
    label = label,
    field = field,
    enabled = enabled,
    inputType = inputType,
    maxLines = maxLines
)
