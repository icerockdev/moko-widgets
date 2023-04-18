/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.ImageResource
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
import dev.icerock.moko.widgets.core.utils.displayLink
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.widget.ButtonWidget
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeCustom
import platform.UIKit.UIControlContentHorizontalAlignment
import platform.UIKit.UIControlContentHorizontalAlignmentCenter
import platform.UIKit.UIControlContentHorizontalAlignmentLeft
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateDisabled
import platform.UIKit.UIControlStateHighlighted
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UISemanticContentAttribute
import platform.UIKit.UISemanticContentAttributeForceLeftToRight
import platform.UIKit.UISemanticContentAttributeForceRightToLeft
import platform.UIKit.bringSubviewToFront
import platform.UIKit.contentEdgeInsets
import platform.UIKit.titleEdgeInsets
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

@Suppress("LongParameterList")
actual class ButtonWithIconViewFactory actual constructor(
    private val background: PressableState<Background<out Fill>>?,
    private val textStyle: TextStyle<PressableState<Color>>?,
    private val isAllCaps: Boolean?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    androidElevationEnabled: Boolean?,
    private val iconGravity: IconGravity?,
    private val iconPadding: Float?,
    private val icon: PressableState<ImageResource>
) : ViewFactory<ButtonWidget<out WidgetSize>> {

    @Suppress("TooGenericExceptionThrown", "ComplexMethod", "LongMethod")
    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val button = UIButton.buttonWithType(
            buttonType = UIButtonTypeCustom
        ).apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyStateBackgroundIfNeeded(background)
            applyTextStyleIfNeeded(textStyle)

            icon.also {
                setImage(it.normal.toUIImage(), forState = UIControlStateNormal)
                setImage(it.pressed.toUIImage(), forState = UIControlStateHighlighted)
                setImage(it.disabled.toUIImage(), forState = UIControlStateDisabled)
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
            else -> throw Exception("Not supported content type")
        }

        widget.enabled?.apply {
            bind { button.setEnabled(it) }
        }

        button.setEventHandler(
            controlEvent = UIControlEventTouchUpInside,
            action = widget.onTap
        )

        val contentAttribute: UISemanticContentAttribute
        val contentAlignment: UIControlContentHorizontalAlignment
        when (iconGravity) {
            IconGravity.START -> {
                contentAttribute = UISemanticContentAttributeForceLeftToRight
                contentAlignment = UIControlContentHorizontalAlignmentLeft
            }
            IconGravity.END -> {
                contentAttribute = UISemanticContentAttributeForceRightToLeft
                contentAlignment = UIControlContentHorizontalAlignmentLeft
            }
            IconGravity.TEXT_START, null -> {
                contentAttribute = UISemanticContentAttributeForceLeftToRight
                contentAlignment = UIControlContentHorizontalAlignmentCenter
            }
            IconGravity.TEXT_END -> {
                contentAttribute = UISemanticContentAttributeForceRightToLeft
                contentAlignment = UIControlContentHorizontalAlignmentCenter
            }
        }
        button.semanticContentAttribute = contentAttribute
        button.contentHorizontalAlignment = contentAlignment

        button.imageView?.let { button.bringSubviewToFront(it) }

        var buttonWidth = 0.0
        // TODO remove this bad :(
        button.displayLink {
            val icPadding: Double = iconPadding?.toDouble() ?: 0.0

            val newButtonWidth = button.bounds.useContents { this.size.width }
            if (buttonWidth == newButtonWidth) return@displayLink
            buttonWidth = newButtonWidth

            val iconWidth = button.imageView?.frame?.useContents { this.size.width } ?: 0.0
            val titleWidth = button.titleLabel?.frame?.useContents { this.size.width } ?: 0.0

            val paddingTop = padding?.top?.toDouble() ?: 0.0
            var paddingLeft = padding?.start?.toDouble() ?: 0.0
            var paddingRight = padding?.end?.toDouble() ?: 0.0
            val paddingBottom = padding?.bottom?.toDouble() ?: 0.0

            val titleLeftInset: CGFloat
            val titleRightInset: CGFloat

            when (iconGravity) {
                IconGravity.START -> {
                    val inset = buttonWidth -
                            iconWidth - titleWidth -
                            paddingLeft - paddingRight

                    titleLeftInset = inset
                    titleRightInset = -inset
                    paddingRight += inset
                }
                IconGravity.END -> {
                    val inset = buttonWidth -
                            iconWidth - titleWidth -
                            paddingLeft - paddingRight

                    titleLeftInset = -inset
                    titleRightInset = inset
                    paddingLeft += inset
                }
                IconGravity.TEXT_START, null -> {
                    titleLeftInset = icPadding
                    titleRightInset = -icPadding
                    paddingRight += icPadding
                }
                IconGravity.TEXT_END -> {
                    titleLeftInset = -icPadding
                    titleRightInset = icPadding
                    paddingLeft += icPadding
                }
            }

            button.titleEdgeInsets = UIEdgeInsetsMake(
                top = 0.0,
                left = titleLeftInset,
                bottom = 0.0,
                right = titleRightInset
            )
            button.contentEdgeInsets = UIEdgeInsetsMake(
                top = paddingTop,
                bottom = paddingBottom,
                left = paddingLeft,
                right = paddingRight
            )
        }

        return ViewBundle(
            view = button,
            size = size,
            margins = margins
        )
    }
}
