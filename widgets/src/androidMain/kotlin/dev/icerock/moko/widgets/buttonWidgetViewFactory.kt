package com.icerockdev.mpp.widgets

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.widgets.databinding.WidgetButtonBinding
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.background.buildBackground
import com.icerockdev.mpp.widgets.style.ext.setDpMargins
import com.icerockdev.mpp.widgets.style.ext.toPlatformSize

actual var buttonWidgetViewFactory: VFC<ButtonWidget> = { context: ViewFactoryContext,
                                                          widget: ButtonWidget ->
    val ctx = context.context
    val dm = ctx.resources.displayMetrics
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetButtonBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_button, parent, false)

    val style = widget.style

    with(binding.buttonWidget) {
        layoutParams = LinearLayout.LayoutParams(
            widget.style.size.width.toPlatformSize(dm),
            widget.style.size.height.toPlatformSize(dm)
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

            setBackground(rippleDrawable)
        }
        setTextColor(style.textStyle.color)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textStyle.size.toFloat())
        isAllCaps = style.isAllCaps
    }

    binding.widget = widget
    binding.setLifecycleOwner(context.lifecycleOwner)
    binding.root
}
