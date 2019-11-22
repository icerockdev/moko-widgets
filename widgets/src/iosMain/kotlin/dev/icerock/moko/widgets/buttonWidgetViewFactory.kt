/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.utils.applyTextStyle
import dev.icerock.moko.widgets.utils.caLayer
import dev.icerock.moko.widgets.utils.displayLink
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CATransaction
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
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


    val button = UIButton.buttonWithType(UIButtonTypeSystem).apply {
        translatesAutoresizingMaskIntoConstraints = false
        setContentHuggingPriority(
            UILayoutPriorityRequired,
            forAxis = UILayoutConstraintAxisHorizontal
        )
        setContentHuggingPriority(
            UILayoutPriorityRequired,
            forAxis = UILayoutConstraintAxisVertical
        )

        applyStateBackground(style.background)

        titleLabel?.applyTextStyle(style.textStyle)

        style.textStyle.color?.also {
            setTintColor(it.toUIColor())
        }
    }


    widget.text.bind {
        button.setTitle(title = it.localized(), forState = UIControlStateNormal)
    }

    button.setEventHandler(UIControlEventTouchUpInside) {
        widget.onTap()
    }

    button
}

fun UIButton.applyStateBackground(background: StateBackground?) {
    if (background == null) return

    adjustsImageWhenDisabled = false
    adjustsImageWhenHighlighted = false

    val normalBg = background.normal.caLayer().also {
        layer.addSublayer(it)
    }
    val disabledBg = background.disabled.caLayer().also {
        layer.addSublayer(it)
    }
    val pressedBg = background.pressed.caLayer().also {
        layer.addSublayer(it)
    }

    fun updateLayers() {
        if (!isEnabled()) {
            disabledBg.opacity = 1.0f
            normalBg.opacity = 0f
            pressedBg.opacity = 0f
            return
        }

        if (isHighlighted()) {
            pressedBg.opacity = 1.0f
            normalBg.opacity = 0f
        } else {
            normalBg.opacity = 1.0f
            pressedBg.opacity = 0f
        }
        disabledBg.opacity = 0f
    }

    updateLayers()

    // FIXME memoryleak, perfomance problem !!!
    displayLink {
        val (width, height) = layer.bounds.useContents { size.width to size.height }

        CATransaction.begin()
        CATransaction.setDisableActions(true)

        normalBg.frame = CGRectMake(0.0, 0.0, width, height)
        disabledBg.frame = CGRectMake(0.0, 0.0, width, height)
        pressedBg.frame = CGRectMake(0.0, 0.0, width, height)

        updateLayers()

        CATransaction.commit()
    }
}
