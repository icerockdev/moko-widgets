package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ContainerWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SystemModalViewFactory(
    background: Background? = null
) : ViewFactory<ContainerWidget<out WidgetSize>>