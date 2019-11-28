/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.SwitchWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.ColorStyle
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultSwitchWidgetViewFactory(
    style: Style = Style()
) : DefaultSwitchWidgetViewFactoryBase

abstract class DefaultSwitchWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<SwitchWidget<out WidgetSize>> {

    /**
     * @property size @see dev.icerock.moko.widgets.style.view.Sized
     * @property padding @see dev.icerock.moko.widgets.style.view.Padded
     * @property margins @see dev.icerock.moko.widgets.style.view.Margined
     * @property background @see dev.icerock.moko.widgets.style.view.Backgrounded
     * @property switchColor switch background, might be null if default
     */
    data class Style(
        override val margins: MarginValues? = null,
        override val background: Background? = null,
        val switchColor: ColorStyle? = null
    ) : Margined, Backgrounded
}
