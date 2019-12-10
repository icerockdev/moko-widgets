/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.applyTextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class DefaultButtonWidgetViewFactory actual constructor(
    style: Style
) : DefaultButtonWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val ctx = viewFactoryContext.androidContext

        // it is hell. Compose save us! ImageButton is ImageView, not Button!
        val button: View = when (widget.content) {
            is ButtonWidget.Content.Text -> {
                Button(ctx).apply {
                    widget.content.text.bind(viewFactoryContext.lifecycleOwner) { text ->
                        this.text = text?.toString(ctx)
                    }
                    applyTextStyle(style.textStyle)
                    style.isAllCaps?.also { isAllCaps = it }
                }
            }
            is ButtonWidget.Content.Icon -> {
                ImageButton(ctx).apply {
                    widget.content.image.bind(viewFactoryContext.lifecycleOwner) { image ->
                        image.loadIn(this)
                    }
                    background = null
                }
            }
        }

        button.applyStyle(style)

        widget.enabled?.bind(viewFactoryContext.lifecycleOwner) { enabled ->
            button.isEnabled = enabled == true
        }

        button.setOnClickListener {
            widget.onTap()
        }

        return ViewBundle(
            view = button,
            size = size,
            margins = style.margins
        )
    }
}
