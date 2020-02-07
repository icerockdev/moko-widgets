package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ContainerWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.WidgetSize

actual class SystemModalViewFactory actual constructor(
    private val background: Background?
) : ViewFactory<ContainerWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: ContainerWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}