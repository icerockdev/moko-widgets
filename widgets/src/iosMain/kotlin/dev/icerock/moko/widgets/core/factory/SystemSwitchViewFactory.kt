/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.widget.SwitchWidget
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISwitch
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemSwitchViewFactory actual constructor(
    private val background: Background<out Fill>?,
    private val tintColor: Color?,
    private val margins: MarginValues?
) : ViewFactory<SwitchWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: SwitchWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val switch = UISwitch(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(background)

            tintColor?.also { onTintColor = it.toUIColor() }
        }

        widget.state.bind { switch.on = it }

        switch.setEventHandler(UIControlEventValueChanged) {
            val on = switch.on
            val current = widget.state.value

            if (on != current) {
                widget.state.value = on
            }
        }

        return ViewBundle(
            view = switch,
            size = size,
            margins = margins
        )
    }
}
