/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.PressableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.TextStyle
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyStateBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.widget.ButtonWidget
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeCustom
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.contentEdgeInsets
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemButtonViewFactory actual constructor(
    private val background: PressableState<Background<out Fill>>?,
    private val textStyle: TextStyle<PressableState<Color>>?,
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

        val buttonType = if (widget.content is ButtonWidget.Content.Icon) {
            UIButtonTypeCustom
        } else {
            UIButtonTypeSystem
        }

        val button = UIButton.buttonWithType(buttonType).apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyStateBackgroundIfNeeded(background)

            applyTextStyleIfNeeded(textStyle)

            padding?.let {
                contentEdgeInsets = UIEdgeInsetsMake(
                    top = it.top.toDouble(),
                    bottom = it.bottom.toDouble(),
                    left = it.start.toDouble(),
                    right = it.end.toDouble()
                )
            }
        }

        val content = widget.content
        when (content) {
            is ButtonWidget.Content.Text -> {
                content.text.bind { text ->
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
                content.image.bind { image ->
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
