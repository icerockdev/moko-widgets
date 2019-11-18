/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.applySize
import dev.icerock.moko.widgets.utils.localized
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeRoundedRect
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UILabel
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var flatAlertWidgetViewFactory: VFC<FlatAlertWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val container = UIView(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false
        applyBackground(style.background)
    }

    val titleLabel = widget.title?.let { title ->
        val titleLabel = UILabel(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            textAlignment = NSTextAlignmentCenter
        }

        title.bind { titleLabel.text = it?.localized() }

        titleLabel
    }

    val messageLabel = UILabel(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false
        textAlignment = NSTextAlignmentCenter
    }
    widget.message.bind { messageLabel.text = it?.localized() }

    val button = widget.buttonText?.let { text ->
        val button = UIButton.buttonWithType(buttonType = UIButtonTypeRoundedRect).apply {
            translatesAutoresizingMaskIntoConstraints = false
        }

        text.bind { button.setTitle(it?.localized(), forState = UIControlStateNormal) }

        button.setEventHandler(UIControlEventTouchUpInside) {
            widget.onTap?.invoke()
        }

        button
    }

    with(container) {
        addSubview(messageLabel)

        // TODO paddings apply

        messageLabel.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
        messageLabel.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true

        if (titleLabel != null) {
            addSubview(titleLabel)

            titleLabel.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
            titleLabel.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true

            titleLabel.topAnchor.constraintEqualToAnchor(topAnchor).active = true
            messageLabel.topAnchor.constraintEqualToAnchor(titleLabel.bottomAnchor).active = true
        } else {
            messageLabel.topAnchor.constraintEqualToAnchor(topAnchor).active = true
        }

        if (button != null) {
            addSubview(button)

            button.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
            button.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
            button.topAnchor.constraintEqualToAnchor(messageLabel.bottomAnchor).active = true
            button.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
        } else {
            messageLabel.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
        }
    }

    container
}
