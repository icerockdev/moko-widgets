/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.view.ViewGroup
import android.widget.TextView
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.old.applyStyle
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.applyPadding
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.utils.bind

actual var textWidgetViewFactory: VFC<TextWidget> = { viewFactoryContext: ViewFactoryContext,
                                                      textWidget: TextWidget ->
    val context = viewFactoryContext.androidContext
    val lifecycleOwner = viewFactoryContext.lifecycleOwner
    val dm = context.resources.displayMetrics
    val style = textWidget.style

    val textView = TextView(context).apply {
        layoutParams = ViewGroup.MarginLayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        ).apply {
            applyMargin(context, style.margins)
        }
        applyStyle(style.textStyle)
        applyPadding(style.padding)
    }

    textWidget.text.bind(lifecycleOwner) { textView.text = it?.toString(context) }

    textView
}
