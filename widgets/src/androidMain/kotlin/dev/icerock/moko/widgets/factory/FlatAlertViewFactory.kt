/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import dev.icerock.moko.widgets.FlatAlertWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.dp

actual class FlatAlertViewFactory actual constructor(
    private val background: Background?,
    private val margins: MarginValues?
) : ViewFactory<FlatAlertWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: FlatAlertWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val rowsContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        rowsContainer.applyBackgroundIfNeeded(background)

        // drawable
        val drawableLiveData = widget.drawable
        if (drawableLiveData != null) {
            val imageView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16.dp(context)
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }

            rowsContainer.addView(imageView)

            drawableLiveData.bind(lifecycleOwner) { resource ->
                if (resource == null) {
                    imageView.setImageDrawable(null)
                    imageView.visibility = View.GONE
                } else {
                    imageView.visibility = View.VISIBLE
                    val drawable = ContextCompat.getDrawable(context, resource.drawableResId)
                    imageView.setImageDrawable(drawable)
                }
            }
        }

        // title
        val titleLiveData = widget.title
        if (titleLiveData != null) {
            val textView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 8.dp(context)
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                gravity = Gravity.CENTER
                TextViewCompat.setTextAppearance(
                    this,
                    android.R.style.TextAppearance_Material_Headline
                )
            }

            rowsContainer.addView(textView)

            titleLiveData.bind(lifecycleOwner) { title ->
                if (title == null) {
                    textView.text = null
                    textView.visibility = View.GONE
                } else {
                    textView.text = title.toString(context)
                    textView.visibility = View.VISIBLE
                }
            }
        }

        // message
        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 8.dp(context)
                gravity = Gravity.CENTER_HORIZONTAL
            }
            gravity = Gravity.CENTER
            TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Material_Subhead)
        }

        rowsContainer.addView(textView)

        widget.message.bind(lifecycleOwner) { msg ->
            if (msg == null) {
                textView.text = null
                textView.visibility = View.GONE
            } else {
                textView.text = msg.toString(context)
                textView.visibility = View.VISIBLE
            }
        }

        // button
        val buttonLiveData = widget.buttonText
        val buttonTap = widget.onTap
        if (buttonLiveData != null && buttonTap != null) {
            val button = AppCompatButton(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                setOnClickListener {
                    buttonTap()
                }
            }

            rowsContainer.addView(button)

            buttonLiveData.bind(lifecycleOwner) { buttonText ->
                if (buttonText == null) {
                    button.text = null
                    button.visibility = View.GONE
                } else {
                    button.text = buttonText.toString(context)
                    button.visibility = View.VISIBLE
                }
            }
        }

        return ViewBundle(
            view = rowsContainer,
            size = size,
            margins = margins
        )
    }
}
