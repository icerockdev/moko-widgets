/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applyStateBackground
import dev.icerock.moko.widgets.utils.applyTextStyle
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.setEventHandler
import platform.CoreGraphics.CGRectEdge
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UILayoutConstraintAxisHorizontal
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.setContentHuggingPriority
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class DefaultButtonWidgetViewFactory actual constructor(
    style: Style
) : DefaultButtonWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
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

            style.padding?.let {
                contentEdgeInsets = UIEdgeInsetsMake(
                    top = it.top.toDouble(),
                    bottom = it.bottom.toDouble(),
                    left = it.start.toDouble(),
                    right = it.end.toDouble()
                )
            }
        }

        widget.text.bind {
            button.setTitle(title = it.localized(), forState = UIControlStateNormal)
        }

        button.setEventHandler(UIControlEventTouchUpInside) {
            widget.onTap()
        }

        return ViewBundle(
            view = button,
            size = size,
            margins = style.margins
        )
    }
}
