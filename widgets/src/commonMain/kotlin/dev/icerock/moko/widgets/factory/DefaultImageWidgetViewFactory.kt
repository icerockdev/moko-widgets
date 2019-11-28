/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ImageWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultImageWidgetViewFactory(
    style: Style = Style()
) : DefaultImageWidgetViewFactoryBase

abstract class DefaultImageWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<ImageWidget<out WidgetSize>> {

    data class Style(
        override val margins: MarginValues? = null,
        val scaleType: ScaleType? = null
    ) : Margined

    enum class ScaleType {
        FILL,
        FIT
    }
}
