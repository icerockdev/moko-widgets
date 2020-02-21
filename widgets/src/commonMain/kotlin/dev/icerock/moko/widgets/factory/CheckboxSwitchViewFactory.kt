/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.SwitchWidget
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.WidgetSize

expect class CheckboxSwitchViewFactory(
    background: Background<out Fill>? = null,
    checkedImage: ImageResource,
    uncheckedImage: ImageResource
) : ViewFactory<SwitchWidget<out WidgetSize>>
