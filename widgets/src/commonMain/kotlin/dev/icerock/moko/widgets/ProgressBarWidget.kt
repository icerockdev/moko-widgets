package com.icerockdev.mpp.widgets

expect var progressBarWidgetViewFactory: VFC<ProgressBarWidget>

class ProgressBarWidget(
    private val factory: VFC<ProgressBarWidget>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
