/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.custom

import android.view.ContextThemeWrapper
import android.view.View
import android.widget.ImageButton
import com.google.android.material.button.MaterialButton
import com.icerockdev.library.R
import dev.icerock.moko.widgets.core.widget.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind

@Suppress("EmptyDefaultConstructor")
actual class MaterialButtonWidgetViewFactory actual constructor() : ViewFactory<ButtonWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val ctx = viewFactoryContext.androidContext

        val content = widget.content
        val button: View = when (content) {
            is ButtonWidget.Content.Text -> {
                MaterialButton(ContextThemeWrapper(ctx, R.style.Theme_MaterialComponents_Light_NoActionBar)).apply {
                    content.text.bind(viewFactoryContext.lifecycleOwner) { text ->
                        this.text = text?.toString(ctx)
                    }
                }
            }
            is ButtonWidget.Content.Icon -> {
                ImageButton(ctx).apply {
                    content.image.bind(viewFactoryContext.lifecycleOwner) { image ->
                        image.loadIn(this)
                    }
                }
            }
        }

        widget.enabled?.bind(viewFactoryContext.lifecycleOwner) { enabled ->
            button.isEnabled = enabled == true
        }

        button.setOnClickListener {
            widget.onTap()
        }

        return ViewBundle(
            view = button,
            size = size,
            margins = null
        )
    }
}
