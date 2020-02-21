/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Border
import dev.icerock.moko.widgets.style.view.CornerRadiusValue
import dev.icerock.moko.widgets.style.view.IOSFieldBorderStyle
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.TextVerticalAlignment
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class MultilineInputViewFactory(
    cornerRadiusValue: CornerRadiusValue?,
    borderColor: Color?,
    borderWidth: Float?,
    backgroundViewColor: Color?,
    margins: MarginValues? = null,
    padding: PaddingValues? = null,
    textStyle: TextStyle? = null,
    labelTextColor: Color? = null,
    textHorizontalAlignment: TextHorizontalAlignment? = null
) : ViewFactory<InputWidget<out WidgetSize>>