/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.SwitchWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SystemSwitchViewFactory(
    background: Background<out Fill>? = null,
    tintColor: Color? = null,
    margins: MarginValues? = null
) : ViewFactory<SwitchWidget<out WidgetSize>>
