package com.icerockdev.mpp.widgets

import android.view.LayoutInflater

actual var progressBarWidgetViewFactory: VFC<ProgressBarWidget> = { context: ViewFactoryContext,
                                                                    widget: ProgressBarWidget ->
    val ctx = context.context
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)

    layoutInflater.inflate(R.layout.widget_progressbar, parent, false)
}
