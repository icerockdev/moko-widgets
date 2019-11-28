/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.FlatAlertWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultFlatAlertWidgetViewFactory(
    style: Style = Style()
) : DefaultFlatAlertWidgetViewFactoryBase

abstract class DefaultFlatAlertWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<FlatAlertWidget<out WidgetSize>> {

    data class Style(
        override val background: Background? = null,
        override val margins: MarginValues? = null
    ) : Backgrounded, Margined
}
