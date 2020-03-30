/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.widget.SwitchWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.CheckableState
import dev.icerock.moko.widgets.core.style.view.WidgetSize

expect class CheckboxSwitchViewFactory(
    background: Background<out Fill>? = null,
    image: CheckableState<ImageResource>
) : ViewFactory<SwitchWidget<out WidgetSize>>
