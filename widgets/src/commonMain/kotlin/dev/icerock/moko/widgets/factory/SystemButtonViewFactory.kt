/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SystemButtonViewFactory(
    background: StateBackground? = null,
    textStyle: TextStyle? = null,
    isAllCaps: Boolean? = null,
    padding: PaddingValues? = null,
    margins: MarginValues? = null
) : ViewFactory<ButtonWidget<out WidgetSize>>
