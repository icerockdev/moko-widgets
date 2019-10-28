package com.icerockdev.mpp.widgets.forms

import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.forms.databinding.WidgetFormBinding

actual var formWidgetViewFactory: VFC<FormWidget> = { context: ViewFactoryContext,
                                                      widget: FormWidget ->

    val ctx = context.context
    val style = widget.style
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetFormBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_form, parent, false)

    binding.widget = widget

    binding.formLayout.apply {
        orientation = when (style.orientation) {
            FormWidget.Group.Orientation.VERTICAL -> LinearLayout.VERTICAL
            FormWidget.Group.Orientation.HORIZONTAL -> LinearLayout.HORIZONTAL
        }

        val density = ctx.resources.displayMetrics.density

        setPadding(
            (style.paddings.start * density).toInt(),
            (style.paddings.top * density).toInt(),
            (style.paddings.end * density).toInt(),
            (style.paddings.bottom * density).toInt()
        )

        clipChildren = false
        clipToPadding = false
    }

    val layoutFactoryContext = ViewFactoryContext(
        context = ctx,
        lifecycleOwner = context.lifecycleOwner,
        parent = binding.formLayout
    )
    widget.items.forEach {
        binding.formLayout.addView(it.buildView(layoutFactoryContext))
    }

    binding.setLifecycleOwner(context.lifecycleOwner)
    binding.root
}