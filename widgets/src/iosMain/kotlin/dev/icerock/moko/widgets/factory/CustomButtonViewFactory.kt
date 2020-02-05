package dev.icerock.moko.widgets.factory

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.*
import dev.icerock.moko.widgets.utils.applyStateBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.setEventHandler
import platform.UIKit.*

actual class CustomButtonViewFactory actual constructor(
    private val background: StateBackground?,
    private val textStyle: TextStyle?,
    private val isAllCaps: Boolean?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    androidElevationEnabled: Boolean?,
    private val textHorizontalAlignment: TextHorizontalAlignment?,
    private val iconHorizontalAlignment: IconHorizontalAlignment?
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