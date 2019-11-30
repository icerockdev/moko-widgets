/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultInputWidgetViewFactory(
    style: Style = Style()
) : DefaultInputWidgetViewFactoryBase

abstract class DefaultInputWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<InputWidget<out WidgetSize>> {

    data class Style(
        override val background: Background? = null,
        override val margins: MarginValues? = null,
        override val padding: PaddingValues? = null,
        val textStyle: TextStyle = TextStyle(),
        val labelTextStyle: TextStyle = TextStyle(),
        val errorTextStyle: TextStyle = TextStyle(),
        val underLineColor: Color? = null,
        val underLineFocusedColor: Color? = null
    ) : Margined, Backgrounded, Padded
}
