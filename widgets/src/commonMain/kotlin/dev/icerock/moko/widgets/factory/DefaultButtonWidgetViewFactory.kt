package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.Margined
import dev.icerock.moko.widgets.style.view.Padded
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.StateBackgrounded
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultButtonWidgetViewFactory(style: Style) :
    DefaultButtonWidgetViewFactoryBase

abstract class DefaultButtonWidgetViewFactoryBase(
    val style: Style
) : ViewFactory<ButtonWidget<WidgetSize>> {

    data class Style(
        override val background: StateBackground? = null,
        override val margins: MarginValues? = null,
        override val padding: PaddingValues? = null,
        val textStyle: TextStyle = TextStyle(),
        val isAllCaps: Boolean? = null
    ) : Margined, StateBackgrounded, Padded
}
