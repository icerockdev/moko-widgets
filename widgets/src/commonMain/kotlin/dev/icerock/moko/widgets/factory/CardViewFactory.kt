package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.CardWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class CardViewFactory(
    padding: PaddingValues? = null,
    margins: MarginValues? = null,
    background: Background? = null
) : ViewFactory<CardWidget<out WidgetSize>>
