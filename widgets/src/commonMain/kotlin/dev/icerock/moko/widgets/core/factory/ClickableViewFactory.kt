/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.ClickableWidget

@Suppress("EmptyDefaultConstructor")
expect class ClickableViewFactory() : ViewFactory<ClickableWidget<out WidgetSize>>
