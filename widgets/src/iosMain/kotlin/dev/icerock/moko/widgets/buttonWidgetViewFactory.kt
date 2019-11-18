/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.localized
import dev.icerock.moko.widgets.utils.setEventHandler
import dev.icerock.plural.cgColors
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CAGradientLayer
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeRoundedRect
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UILayoutConstraintAxisHorizontal
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.setContentHuggingPriority
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var buttonWidgetViewFactory: VFC<ButtonWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val button = UIButton.buttonWithType(buttonType = UIButtonTypeRoundedRect).apply {
        translatesAutoresizingMaskIntoConstraints = false
        setContentHuggingPriority(UILayoutPriorityRequired, forAxis = UILayoutConstraintAxisHorizontal)
        setContentHuggingPriority(UILayoutPriorityRequired, forAxis = UILayoutConstraintAxisVertical)
        applyBackground(style.background)
    }

    widget.text.bind {
        button.setTitle(title = it.localized(), forState = UIControlStateNormal)
    }

    button.setEventHandler(UIControlEventTouchUpInside) {
        widget.onTap()
    }

    button
}
