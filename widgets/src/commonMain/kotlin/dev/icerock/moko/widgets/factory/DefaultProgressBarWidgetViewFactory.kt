/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.ProgressBarWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultProgressBarWidgetViewFactory(
    style: Style = Style()
) : DefaultProgressBarWidgetViewFactoryBase

abstract class DefaultProgressBarWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<ProgressBarWidget<out WidgetSize>> {

    data class Style(
        override val margins: MarginValues? = null,
        val color: Color? = null
    ) : Margined
}
