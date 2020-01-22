/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SystemListViewFactory(
    background: Background? = null,
    dividerEnabled: Boolean? = null,
    reversed: Boolean = false,
    padding: PaddingValues? = null,
    margins: MarginValues? = null
) : ViewFactory<ListWidget<out WidgetSize>>
