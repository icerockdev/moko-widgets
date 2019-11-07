/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.widget.Button
import androidx.lifecycle.Observer
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.applyTextStyle
import dev.icerock.moko.widgets.style.withSize

actual var buttonWidgetViewFactory: VFC<ButtonWidget> = { context: ViewFactoryContext,
                                                          widget: ButtonWidget ->
    val ctx = context.androidContext
    val style = widget.style

    val button = Button(ctx).apply {
        applyTextStyle(style.textStyle)

        style.isAllCaps?.also { isAllCaps = it }
    }

    val enabledLiveData = widget.enabled
    if (enabledLiveData != null) {
        enabledLiveData.ld().observe(context.lifecycleOwner, Observer { enabled ->
            button.isEnabled = enabled == true
        })
    }

    button.setOnClickListener {
        widget.onTap()
    }

    widget.text.ld().observe(context.lifecycleOwner, Observer { text ->
        button.text = text?.toString(ctx)
    })

    button.withSize(style.size).apply { applyStyle(style) }
}
