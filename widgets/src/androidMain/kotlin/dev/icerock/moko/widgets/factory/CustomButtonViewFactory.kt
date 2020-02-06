package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.StateBackground
import dev.icerock.moko.widgets.style.view.*

actual class CustomButtonViewFactory actual constructor(
    private val background: StateBackground?,
    private val textStyle: TextStyle?,
    private val isAllCaps: Boolean?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    androidElevationEnabled: Boolean?,
    private val textInset: Inset?,
    private val iconInset: Inset?
) : ViewFactory<ButtonWidget<out WidgetSize>> {
    override fun <WS : WidgetSize> build(
        widget: ButtonWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}