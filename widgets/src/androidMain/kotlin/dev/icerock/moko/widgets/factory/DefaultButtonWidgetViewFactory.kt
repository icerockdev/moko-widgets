/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.widget.Button
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
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

        val button = Button(ctx).apply {
            applyTextStyle(style.textStyle)
            applyStyle(style)

            style.isAllCaps?.also { isAllCaps = it }
        }

        widget.enabled?.bind(viewFactoryContext.lifecycleOwner) { enabled ->
            button.isEnabled = enabled == true
        }

        button.setOnClickListener {
            widget.onTap()
        }

        widget.text.bind(viewFactoryContext.lifecycleOwner) { text ->
            button.text = text?.toString(ctx)
        }

        return ViewBundle(
            view = button,
            size = size,
            margins = style.margins
        )
    }
}
