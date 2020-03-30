/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.CheckableState
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.widget.SwitchWidget
import platform.UIKit.UIButton
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class CheckboxSwitchViewFactory actual constructor(
    private val background: Background<out Fill>?,
    private val image: CheckableState<ImageResource>
) : ViewFactory<SwitchWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: SwitchWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val btn = UIButton().apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyBackgroundIfNeeded(background)
        }

        widget.state.bind { state ->
            val imgRes = if (state) image.checked else image.unchecked
            btn.setImage(image = imgRes.toUIImage(), forState = UIControlStateNormal)
        }

        btn.setEventHandler(UIControlEventTouchUpInside) {
            widget.state.value = !widget.state.value
        }

        return ViewBundle(
            view = btn,
            size = size,
            margins = null
        )
    }
}
