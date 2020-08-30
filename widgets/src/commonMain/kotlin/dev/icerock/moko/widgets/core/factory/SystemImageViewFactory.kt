/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.ImageWidget

expect class SystemImageViewFactory(
    margins: MarginValues? = null,
    cornerRadius: Float? = null
) : ViewFactory<ImageWidget<out WidgetSize>>
