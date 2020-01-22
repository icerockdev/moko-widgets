/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applyStateBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.setEventHandler
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemButtonViewFactory actual constructor(
    private val background: StateBackground?,
    private val textStyle: TextStyle?,
    private val isAllCaps: Boolean?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    androidElevationEnabled: Boolean?
) : ViewFactory<ButtonWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val button = UIButton.buttonWithType(UIButtonTypeSystem).apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyStateBackgroundIfNeeded(background)

            titleLabel?.applyTextStyleIfNeeded(textStyle)

            textStyle?.color?.also { setTintColor(it.toUIColor()) }

            padding?.let {
                contentEdgeInsets = UIEdgeInsetsMake(
                    top = it.top.toDouble(),
                    bottom = it.bottom.toDouble(),
                    left = it.start.toDouble(),
                    right = it.end.toDouble()
                )
            }
        }

        when (widget.content) {
            is ButtonWidget.Content.Text -> {
                widget.content.text.bind { text ->
                    val localizedText = text?.localized()
                    val processedText = if (isAllCaps == true) {
                        localizedText?.toUpperCase()
                    } else {
                        localizedText
                    }
                    button.setTitle(title = processedText, forState = UIControlStateNormal)
                }
            }
            is ButtonWidget.Content.Icon -> {
                widget.content.image.bind { image ->
                    image.apply(button) {
                        button.setImage(it, forState = UIControlStateNormal)
                    }
                }
            }
        }

        widget.enabled?.apply {
            bind { button.setEnabled(it) }
        }

        button.setEventHandler(UIControlEventTouchUpInside) {
            widget.onTap()
        }

        return ViewBundle(
            view = button,
            size = size,
            margins = margins
        )
    }
}
