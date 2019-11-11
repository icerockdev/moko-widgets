/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.RequireId
import dev.icerock.moko.widgets.core.Styled
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.Sized
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget>

@WidgetDef
class SingleChoiceWidget(
    val factory: VFC<SingleChoiceWidget>,
    override val style: Style,
    override val id: Id,
    val field: FormField<Int?, StringDesc>,
    val label: LiveData<StringDesc>,
    val cancelLabel: LiveData<StringDesc>,
    val values: LiveData<List<StringDesc>>
) : Widget(), Styled<SingleChoiceWidget.Style>, RequireId<SingleChoiceWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this)
    }

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property underLineColor color of the underline
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property dropDownBackground widget's dropdown view background, might be null if not required
     */
    data class Style(
        override val size: WidgetSize = WidgetSize.Const(),
        override val margins: MarginValues? = null,
        override val padding: PaddingValues? = null,
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val dropDownTextColor: Color? = null,
        val underlineColor: Color? = null,
        val dropDownBackground: Background? = null
    ) : Widget.Style, Margined, Sized, Padded

    interface Id : WidgetScope.Id
}
