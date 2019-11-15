/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.applySize
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISwitch
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var switchWidgetViewFactory: VFC<SwitchWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val switch = UISwitch(frame = CGRectZero.readValue())
    switch.translatesAutoresizingMaskIntoConstraints = false

    widget.state.bind { switch.on = it }

    switch.setEventHandler(UIControlEventValueChanged) {
        val on = switch.on
        val current = widget.state.value

        if (on != current) {
            widget.state.value = on
        }
    }

    switch.applySize(style.size)
}
