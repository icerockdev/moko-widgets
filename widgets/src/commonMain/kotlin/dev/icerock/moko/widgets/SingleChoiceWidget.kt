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
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget>

class SingleChoiceWidget(
    private val factory: VFC<SingleChoiceWidget>,
    val style: Style,
    val field: FormField<Int?, StringDesc>,
    val label: LiveData<StringDesc>,
    val cancelLabel: LiveData<StringDesc>,
    val values: LiveData<List<StringDesc>>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property underLineColor color of the underline
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property dropDownBackground widget's dropdown view background, might be null if not required
     */
    data class Style(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val dropDownTextColor: Color? = null,
        val underlineColor: Color? = null,
        override val margins: MarginValues = MarginValues(),
        val dropDownBackground: Background? = null
    ) : Margined

    object FactoryKey : WidgetScope.Key
    object StyleKey : WidgetScope.Key
}

val WidgetScope.singleChoiceFactory: VFC<SingleChoiceWidget>
        by WidgetScope.readProperty(SingleChoiceWidget.FactoryKey, ::singleChoiceWidgetViewFactory)

var WidgetScope.Builder.singleChoiceFactory: VFC<SingleChoiceWidget>
        by WidgetScope.readWriteProperty(SingleChoiceWidget.FactoryKey, WidgetScope::singleChoiceFactory)

val WidgetScope.singleChoiceStyle: SingleChoiceWidget.Style
        by WidgetScope.readProperty(SingleChoiceWidget.StyleKey) { SingleChoiceWidget.Style() }

var WidgetScope.Builder.singleChoiceStyle: SingleChoiceWidget.Style
        by WidgetScope.readWriteProperty(SingleChoiceWidget.StyleKey, WidgetScope::singleChoiceStyle)

fun WidgetScope.singleChoice(
    factory: VFC<SingleChoiceWidget> = this.singleChoiceFactory,
    style: SingleChoiceWidget.Style = this.singleChoiceStyle,
    field: FormField<Int?, StringDesc>,
    label: LiveData<StringDesc>,
    cancelLabel: LiveData<StringDesc>,
    values: LiveData<List<StringDesc>>
) = SingleChoiceWidget(
    factory = factory,
    style = style,
    field = field,
    label = label,
    cancelLabel = cancelLabel,
    values = values
)