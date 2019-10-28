package com.icerockdev.mpp.widgets

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.widgets.databinding.WidgetFlatAlertBinding

actual var flatAlertWidgetViewFactory: VFC<FlatAlertWidget> = { context: ViewFactoryContext,
                                                                widget: FlatAlertWidget ->
    val ctx = context.context
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    // TODO добавить стилизацию для алерта из-вне (ic_empty, ic_error, цвета выдернуть из модуля)
    val binding: WidgetFlatAlertBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_flat_alert, parent, false)
    binding.widget = widget
    binding.lifecycleOwner = context.lifecycleOwner
    binding.root
}
