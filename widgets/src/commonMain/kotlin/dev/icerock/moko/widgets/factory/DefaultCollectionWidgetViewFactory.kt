/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.Backgrounded
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultCollectionWidgetViewFactory(
    style: Style = Style()
) : DefaultCollectionWidgetViewFactoryBase

abstract class DefaultCollectionWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<CollectionWidget<out WidgetSize>> {

    data class Style(
        override val background: Background? = null,
        override val padding: PaddingValues? = null,
        override val margins: MarginValues? = null,
        val orientation: Orientation = Orientation.VERTICAL,
        val spanCount: Int = 2,
        val reversed: Boolean = false
    ) : Padded, Margined, Backgrounded
}
