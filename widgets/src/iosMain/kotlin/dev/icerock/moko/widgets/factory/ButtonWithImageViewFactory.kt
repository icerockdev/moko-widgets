/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.*
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.utils.*
import kotlinx.cinterop.useContents
import platform.UIKit.*

actual class ButtonWithImageViewFactory actual constructor(
    private val background: StateBackground?,
    private val textStyle: TextStyle?,
    private val isAllCaps: Boolean?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    androidElevationEnabled: Boolean?,
    private val icon: Value<Image>,
    private val titleLeftIconRight: Boolean?,
    private val contentIndentFromBorder: Double?
) : ViewFactory<ButtonWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        val buttonType = UIButtonTypeCustom

        val button = UIButton.buttonWithType(buttonType).apply {
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
            else -> throw Exception("Not supported content type")
        }

        icon.bind { image ->
            image.apply(button) {
                button.setImage(it, forState = UIControlStateNormal)
            }
        }


        titleLeftIconRight?.let {
            button.displayLink {

                val contentIndentFromBorder: Double = contentIndentFromBorder ?: 0.0

                val buttonWidth = button.layer
                    .bounds
                    .useContents { this.size.width }
                val iconWidth = button.imageView!!
                    .frame
                    .useContents { this.size.width }
                val titleWidth = button.titleLabel!!
                    .frame
                    .useContents { this.size.width }

                if (it) {
                   button.imageEdgeInsets = UIEdgeInsetsMake(
                       top = 0.0,
                       left = buttonWidth - iconWidth - contentIndentFromBorder,
                       bottom = 0.0,
                       right = 0.0
                   )
                    button.titleEdgeInsets = UIEdgeInsetsMake(
                        top = 0.0,
                        left = - (buttonWidth - titleWidth - contentIndentFromBorder * 2),
                        bottom = 0.0,
                        right = iconWidth
                    )
                } else {
                    button.imageEdgeInsets = UIEdgeInsetsMake(
                        top = 0.0,
                        left = contentIndentFromBorder,
                        bottom = 0.0,
                        right = buttonWidth - iconWidth - contentIndentFromBorder
                    )
                    button.titleEdgeInsets = UIEdgeInsetsMake(
                        top = 0.0,
                        left = buttonWidth - titleWidth - iconWidth - contentIndentFromBorder * 2,
                        bottom = 0.0,
                        right = 0.0
                    )
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