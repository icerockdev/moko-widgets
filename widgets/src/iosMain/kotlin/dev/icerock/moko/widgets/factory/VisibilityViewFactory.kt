package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.VisibilityWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bind
import platform.UIKit.hidden

actual class VisibilityViewFactory actual constructor() : ViewFactory<VisibilityWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: VisibilityWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = widget.child.buildView(viewFactoryContext) as ViewBundle<WS>

        widget.showed.bind { bundle.view.hidden = it.not() }

        return bundle
    }
}