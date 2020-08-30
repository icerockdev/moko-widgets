/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.collection

import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.background.Orientation
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect class SimpleCollectionViewFactory(
    orientation: Orientation = Orientation.VERTICAL,
    padding: PaddingValues? = null,
    margins: MarginValues? = null,
    background: Background<Fill.Solid>? = null
) : ViewFactory<CollectionWidget<out WidgetSize>>
