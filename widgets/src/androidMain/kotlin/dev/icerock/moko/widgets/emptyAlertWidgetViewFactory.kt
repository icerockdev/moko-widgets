package com.icerockdev.mpp.widgets

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.widgets.databinding.WidgetEmptyAlertBinding

actual var emptyAlertWidgetViewFactory: VFC<EmptyAlertWidget> = { context: ViewFactoryContext,
                                                                  widget: EmptyAlertWidget ->
    val ctx = context.context
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    // TODO добавить стилизацию для алерта из-вне (ic_empty, ic_error, цвета выдернуть из модуля)
    val binding: WidgetEmptyAlertBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_empty_alert, parent, false)
    binding.widget = widget
    binding.lifecycleOwner = context.lifecycleOwner
    binding.root
}
