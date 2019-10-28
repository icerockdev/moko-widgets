package com.icerockdev.mpp.widgets.forms

import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.forms.databinding.WidgetHeaderBinding
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.background.buildBackground
import com.icerockdev.mpp.widgets.style.ext.setDpMargins

actual var headerWidgetViewFactory: VFC<HeaderWidget> = { context: ViewFactoryContext,
                                                          widget: HeaderWidget ->
    val layoutInflater = LayoutInflater.from(context.context)
    val binding: WidgetHeaderBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_header, context.parent, false)

    binding.widget = widget

    val style = widget.style

    with(binding.textView) {
        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
            setDpMargins(
                context.context.resources,
                style.margins.start, style.margins.top, style.margins.end, style.margins.bottom
            )
        }

        setTextColor(style.textStyle.color)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textStyle.size.toFloat())

        style.background?.let {
            background = it.buildBackground(context.context)
        }
    }

    binding.setLifecycleOwner(context.lifecycleOwner)

    binding.root
}
