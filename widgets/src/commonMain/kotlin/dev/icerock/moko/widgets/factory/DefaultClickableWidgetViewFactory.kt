/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ClickableWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class DefaultClickableWidgetViewFactory() : DefaultClickableWidgetViewFactoryBase

abstract class DefaultClickableWidgetViewFactoryBase(
) : ViewFactory<ClickableWidget<out WidgetSize>> {
}
