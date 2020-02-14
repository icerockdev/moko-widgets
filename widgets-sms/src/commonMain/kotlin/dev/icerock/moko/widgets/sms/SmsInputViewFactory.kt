/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sms

import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class SmsInputViewFactory(wrapped: ViewFactory<InputWidget<out WidgetSize>>) :
    ViewFactory<InputWidget<out WidgetSize>>