package com.icerockdev.mpp.widgets

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.widgets.databinding.WidgetTextBinding
import com.icerockdev.mpp.widgets.style.background.Background

actual var textWidgetViewFactory: VFC<TextWidget> = { context: ViewFactoryContext,
                                                      widget: TextWidget ->
    val ctx = context.context
    val lifecycleOwner = context.lifecycleOwner
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetTextBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_text, parent, false)
    binding.widget = widget
    binding.setLifecycleOwner(lifecycleOwner)
    binding.root

    binding.root
}
