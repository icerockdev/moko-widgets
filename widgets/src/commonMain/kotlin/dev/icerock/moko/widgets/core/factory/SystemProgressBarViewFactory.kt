/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.widget.ProgressBarWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect class SystemProgressBarViewFactory(
    margins: MarginValues? = null,
    color: Color? = null
) : ViewFactory<ProgressBarWidget<out WidgetSize>>
