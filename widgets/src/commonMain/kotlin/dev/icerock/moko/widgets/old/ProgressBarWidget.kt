package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var progressBarWidgetViewFactory: VFC<ProgressBarWidget>

class ProgressBarWidget(
    private val factory: VFC<ProgressBarWidget>
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
