/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.SingleChoiceWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultSingleChoiceWidgetViewFactory(
    style: Style = Style()
) : DefaultSingleChoiceWidgetViewFactoryBase

abstract class DefaultSingleChoiceWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<SingleChoiceWidget<out WidgetSize>> {

    /**
     * @property size desired size of widget
     * @property textStyle style of user input text
     * @property labelTextStyle floating label text style
     * @property underLineColor color of the underline
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property dropDownBackground widget's dropdown view background, might be null if not required
     */
    data class Style(
        override val margins: MarginValues? = null,
        override val padding: PaddingValues? = null,
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val dropDownTextColor: Color? = null,
        val underlineColor: Color? = null,
        val dropDownBackground: Background? = null
    ) : Margined, Padded
}
