/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SystemCollectionViewFactory(
    orientation: Orientation = Orientation.VERTICAL,
    spanCount: Int = 2,
    padding: PaddingValues? = null,
    margins: MarginValues? = null,
    background: Background<Fill.Solid>? = null
) : ViewFactory<CollectionWidget<out WidgetSize>>
