/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize

data class ViewBundle<WS : WidgetSize>(
    val view: View,
    // all information that affects anything outside the view itself
    val size: WS,
    val margins: MarginValues?
)