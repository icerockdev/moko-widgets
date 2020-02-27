/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.annotation.SuppressLint
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.TextWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.applyTextStyleIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.TextHorizontalAlignment
import dev.icerock.moko.widgets.style.view.TextStyle
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind

actual class SystemTextViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val textStyle: TextStyle<Color>?,
    private val textHorizontalAlignment: TextHorizontalAlignment?,
    private val margins: MarginValues?,
    private val isHtmlConverted: Boolean
) : ViewFactory<TextWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: TextWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.androidContext
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val textView = AppCompatTextView(context).apply {
            applyTextStyleIfNeeded(textStyle)
            applyBackgroundIfNeeded(this@SystemTextViewFactory.background)

            @SuppressLint("RtlHardcoded")
            when (this@SystemTextViewFactory.textHorizontalAlignment) {
                TextHorizontalAlignment.LEFT -> gravity = Gravity.LEFT
                TextHorizontalAlignment.CENTER -> gravity = Gravity.CENTER
                TextHorizontalAlignment.RIGHT -> gravity = Gravity.RIGHT
                null -> {
                }
            }
        }
        val strProcessing: (String) -> CharSequence = if (!isHtmlConverted) {
            { string -> string }
        } else {
            { string ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(string)
                }
            }
        }
        widget.maxLines?.bind(lifecycleOwner) {
            textView.maxLines = (it ?: Int.MAX_VALUE)
            textView.ellipsize = TextUtils.TruncateAt.END
        }
        widget.text.bind(lifecycleOwner) {
            textView.text = it?.toString(context)?.run(strProcessing)
        }

        if (isHtmlConverted) {
            textView.movementMethod = LinkMovementMethod.getInstance()
        }

        return ViewBundle(
            view = textView,
            size = size,
            margins = margins
        )
    }
}