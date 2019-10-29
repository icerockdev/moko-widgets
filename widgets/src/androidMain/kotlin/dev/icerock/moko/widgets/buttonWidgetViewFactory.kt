package dev.icerock.moko.widgets

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.buildBackground
import dev.icerock.moko.widgets.style.ext.setDpMargins
import dev.icerock.moko.widgets.style.ext.toPlatformSize

actual var buttonWidgetViewFactory: VFC<ButtonWidget> = { context: ViewFactoryContext,
                                                          widget: ButtonWidget ->
    val ctx = context.context
    val dm = ctx.resources.displayMetrics
    val parent = context.parent
    val style = widget.style

    val button = Button(ctx).apply {
        layoutParams = LinearLayout.LayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        ).apply {
            setDpMargins(
                resources = ctx.resources,
                marginStart = style.margins.start,
                marginTop = style.margins.top,
                marginEnd = style.margins.end,
                marginBottom = style.margins.bottom
            )
        }

        style.background?.let {
            val rippleDrawable = RippleDrawable(
                ColorStateList.valueOf(Color.GRAY),
                it.buildBackground(ctx), null
            )

            background = rippleDrawable
        }
        setTextColor(style.textStyle.color)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textStyle.size.toFloat())
        isAllCaps = style.isAllCaps
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

    button
}
