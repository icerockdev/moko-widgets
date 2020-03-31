/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.widget.SingleChoiceWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect class SystemSingleChoiceViewFactory(
    textStyle: TextStyle<Color>? = null,
    labelTextStyle: TextStyle<Color>? = null,
    dropDownTextColor: Color? = null,
    underlineColor: Color? = null,
    dropDownBackground: Background<Fill.Solid>? = null,
    padding: PaddingValues? = null,
    margins: MarginValues? = null
) : ViewFactory<SingleChoiceWidget<out WidgetSize>>
