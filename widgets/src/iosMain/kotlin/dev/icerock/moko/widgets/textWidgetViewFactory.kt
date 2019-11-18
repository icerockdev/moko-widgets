/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.applySize
import dev.icerock.moko.widgets.utils.localized
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UILabel
import platform.UIKit.UILayoutConstraintAxisHorizontal
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UILayoutPriorityDefaultHigh
import platform.UIKit.setContentCompressionResistancePriority
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var textWidgetViewFactory: VFC<TextWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val label = UILabel(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false
        applyBackground(style.background)

        setContentCompressionResistancePriority(UILayoutPriorityDefaultHigh, UILayoutConstraintAxisVertical)
        setContentCompressionResistancePriority(UILayoutPriorityDefaultHigh, UILayoutConstraintAxisHorizontal)
    }

    widget.text.bind {
        label.text = it.localized()
    }

    label
}
