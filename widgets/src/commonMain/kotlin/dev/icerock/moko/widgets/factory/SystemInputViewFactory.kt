/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.*

expect class SystemInputViewFactory(
    background: Background? = null,
    margins: MarginValues? = null,
    padding: PaddingValues? = null,
    textStyle: TextStyle? = null,
    labelTextColor: Color? = null,
    textAlignment: TextAlignment? = null
) : ViewFactory<InputWidget<out WidgetSize>>
