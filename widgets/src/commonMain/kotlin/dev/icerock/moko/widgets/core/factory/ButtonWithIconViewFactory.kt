/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.PressableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.ButtonWidget

expect class ButtonWithIconViewFactory(
    background: PressableState<Background<out Fill>>? = null,
    textStyle: TextStyle<PressableState<Color>>? = null,
    isAllCaps: Boolean? = null,
    padding: PaddingValues? = null,
    margins: MarginValues? = null,
    androidElevationEnabled: Boolean? = null,
    iconGravity: IconGravity? = null,
    iconPadding: Float? = null,
    icon: PressableState<ImageResource>
) : ViewFactory<ButtonWidget<out WidgetSize>>

enum class IconGravity {
    START,
    END,
    TEXT_START,
    TEXT_END
}
