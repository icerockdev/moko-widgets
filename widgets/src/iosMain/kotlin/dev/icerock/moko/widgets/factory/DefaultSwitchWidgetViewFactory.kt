/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.SwitchWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISwitch
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class DefaultSwitchWidgetViewFactory actual constructor(
    style: Style
) : DefaultSwitchWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: SwitchWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val switch = UISwitch(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackground(style.background)

            // TODO add color style support
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
            margins = style.margins
        )
    }
}
