/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.Observer
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.withSize
import dev.icerock.moko.widgets.utils.dp
import dev.icerock.moko.widgets.view.MarginedLinearLayout

actual var flatAlertWidgetViewFactory: VFC<FlatAlertWidget> = { context: ViewFactoryContext,
                                                                widget: FlatAlertWidget ->
    val ctx = context.context
    val style = widget.style

    val rowsContainer = MarginedLinearLayout(ctx).apply {
        orientation = LinearLayout.VERTICAL
    }

    // drawable
    val drawableLiveData = widget.drawable
    if (drawableLiveData != null) {
        val imageView = ImageView(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16.dp(ctx)
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }

        rowsContainer.addView(imageView)

        drawableLiveData.ld().observe(context.lifecycleOwner, Observer { resource ->
            if (resource == null) {
                imageView.setImageDrawable(null)
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(ctx.getDrawable(resource.drawableResId))
            }
        })
    }

    // title
    val titleLiveData = widget.title
    if (titleLiveData != null) {
        val textView = TextView(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 8.dp(ctx)
                gravity = Gravity.CENTER_HORIZONTAL
            }
            gravity = Gravity.CENTER
            TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Material_Headline)
        }

        rowsContainer.addView(textView)

        titleLiveData.ld().observe(context.lifecycleOwner, Observer { title ->
            if (title == null) {
                textView.text = null
                textView.visibility = View.GONE
            } else {
                textView.text = title.toString(ctx)
                textView.visibility = View.VISIBLE
            }
        })
    }

    // message
    val textView = TextView(ctx).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 8.dp(ctx)
            gravity = Gravity.CENTER_HORIZONTAL
        }
        gravity = Gravity.CENTER
        TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Material_Subhead)
    }

    rowsContainer.addView(textView)

    widget.message.ld().observe(context.lifecycleOwner, Observer { msg ->
        if (msg == null) {
            textView.text = null
            textView.visibility = View.GONE
        } else {
            textView.text = msg.toString(ctx)
            textView.visibility = View.VISIBLE
        }
    })

    // button
    val buttonLiveData = widget.buttonText
    val buttonTap = widget.onTap
    if (buttonLiveData != null && buttonTap != null) {
        val button = AppCompatButton(ctx).apply {
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

        buttonLiveData.ld().observe(context.lifecycleOwner, Observer { buttonText ->
            if (buttonText == null) {
                button.text = null
                button.visibility = View.GONE
            } else {
                button.text = buttonText.toString(ctx)
                button.visibility = View.VISIBLE
            }
        })
    }

    rowsContainer.withSize(style.size).apply { applyStyle(style) }
}
