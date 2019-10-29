/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.fields.FormField
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.WidgetSize

expect var dateWidgetViewFactory: VFC<DateWidget>

class DateWidget(
    private val factory: VFC<DateWidget> = dateWidgetViewFactory,
    val label: LiveData<StringDesc>,
    val field: FormField<Date?, StringDesc>,
    val style: DateFieldStyle = DateFieldStyle()
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property underLineColor color of the underline
     * @property margins @see <com.icerockdev.mpp.widget.style.view.Margined>
     * @property background widget's background, can be null if none required
     */
    data class DateFieldStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val errorTextStyle: TextStyle = labelTextStyle,
        val underLineColor: Int = 0xFF000000.toInt(),
        override val margins: MarginValues = MarginValues(),
        val background: Background? = null
    ) : Margined
}

sealed class Date(
    val year: Int,
    val month: Int,
    val day: Int
) {
    override fun toString(): String {
        return "$year-$month-$day"
    }

    object Empty : Date(0, 0, 0)

    object Incomplete : Date(0, 0, 0)

    class Filled(year: Int, month: Int, day: Int) : Date(year, month, day)
}
