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
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var textWidgetViewFactory: VFC<TextWidget>

@WidgetDef
class TextWidget(
    private val factory: VFC<TextWidget>,
    val text: LiveData<StringDesc>,
    val style: Style,
    val id: Id?
) : Widget() {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    data class Style(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        override val padding: PaddingValues = PaddingValues(),
        override val margins: MarginValues = MarginValues()
    ) : Padded, Margined

//    data class HeaderStyle(

//    ) : Margined
//        val background: Background? = null
//        val underlineColor: Int = 0xFF000000.toInt(),
//        override val margins: MarginValues = MarginValues(),
//        val textStyle: TextStyle = TextStyle(),
//        val size: WidgetSize = WidgetSize(),

    interface Id : WidgetScope.Id
}
