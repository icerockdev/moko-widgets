/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.flat

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.InputWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class FlatInputViewFactory(
    platformDependency: PlatformDependency,
    textColor: Color? = null,
    textSize: Int? = null,
    backgroundColor: Color? = null
) : ViewFactory<InputWidget<out WidgetSize>> {
    interface PlatformDependency
}
