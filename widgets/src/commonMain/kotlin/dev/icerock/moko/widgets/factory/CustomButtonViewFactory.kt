package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.*

expect class CustomButtonViewFactory(
    background: StateBackground? = null,
    textStyle: TextStyle? = null,
    isAllCaps: Boolean? = null,
    padding: PaddingValues? = null,
    margins: MarginValues? = null,
    androidElevationEnabled: Boolean? = null,
    textHorizontalAlignment: TextHorizontalAlignment? = null,
    iconHorizontalAlignment: IconHorizontalAlignment? = null
) : ViewFactory<ButtonWidget<out WidgetSize>>

