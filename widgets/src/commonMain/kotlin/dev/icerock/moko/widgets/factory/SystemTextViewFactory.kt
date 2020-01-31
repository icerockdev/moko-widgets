/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SystemTextViewFactory(
    background: Background? = null,
    textStyle: TextStyle? = null,
    textHorizontalAlignment: TextHorizontalAlignment? = null,
    margins: MarginValues? = null,
    isHtmlConverted: Boolean = false
) : ViewFactory<TextWidget<out WidgetSize>>
