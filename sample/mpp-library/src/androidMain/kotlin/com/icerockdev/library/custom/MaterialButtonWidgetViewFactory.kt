/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.custom

import android.view.ContextThemeWrapper
import com.google.android.material.button.MaterialButton
import com.icerockdev.library.R
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class MaterialButtonWidgetViewFactory actual constructor() : ViewFactory<ButtonWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val ctx = viewFactoryContext.androidContext

        val button = MaterialButton(ContextThemeWrapper(ctx, R.style.Theme_MaterialComponents_Light_NoActionBar))

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
            margins = null
        )
    }
}
