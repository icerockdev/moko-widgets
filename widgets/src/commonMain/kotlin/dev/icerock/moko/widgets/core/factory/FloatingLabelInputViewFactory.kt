/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.FocusableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.InputWidget

@Suppress("LongParameterList")
expect class FloatingLabelInputViewFactory(
    background: Background<Fill.Solid>? = null,
    margins: MarginValues? = null,
    padding: PaddingValues? = null,
    textStyle: TextStyle<Color>? = null,
    labelTextStyle: TextStyle<Color>? = null,
    errorTextStyle: TextStyle<Color>? = null,
    underLineColor: FocusableState<Color>? = null,
    textHorizontalAlignment: TextHorizontalAlignment? = null
) : ViewFactory<InputWidget<out WidgetSize>>
