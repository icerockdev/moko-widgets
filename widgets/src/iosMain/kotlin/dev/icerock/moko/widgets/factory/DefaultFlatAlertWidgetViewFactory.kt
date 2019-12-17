/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.FlatAlertWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.bind
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

actual class DefaultFlatAlertWidgetViewFactory actual constructor(
    style: Style
) : DefaultFlatAlertWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: FlatAlertWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(style.background)
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

            messageLabel.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
            messageLabel.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true

            if (titleLabel != null) {
                addSubview(titleLabel)

                titleLabel.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
                titleLabel.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true

                titleLabel.topAnchor.constraintEqualToAnchor(topAnchor).active = true
                messageLabel.topAnchor.constraintEqualToAnchor(titleLabel.bottomAnchor).active =
                    true
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

        return ViewBundle(
            view = container,
            size = size,
            margins = style.margins
        )
    }
}
