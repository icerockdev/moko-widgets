package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.*

expect class FloatingLabelInputViewFactory(
    background: Background? = null,
    margins: MarginValues? = null,
    padding: PaddingValues? = null,
    textStyle: TextStyle? = null,
    labelTextStyle: TextStyle? = null,
    errorTextStyle: TextStyle? = null,
    underLineColor: Color? = null,
    underLineFocusedColor: Color? = null,
    textAlignment: TextAlignment? = null
) : ViewFactory<InputWidget<out WidgetSize>>
