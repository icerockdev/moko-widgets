package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.view.MarginValues
import com.icerockdev.mpp.widgets.style.view.Margined
import com.icerockdev.mpp.widgets.style.view.TextStyle
import com.icerockdev.mpp.widgets.style.view.WidgetSize
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var headerWidgetViewFactory: VFC<HeaderWidget>

class HeaderWidget(
    private val factory: VFC<HeaderWidget> = headerWidgetViewFactory,
    val text: LiveData<StringDesc>,
    val style: HeaderStyle = HeaderStyle()
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    /**
     * @property size desired size of widget
     * @property textStyle style header's text
     * @property underLineColor color of the underline
     * @property margins @see com.icerockdev.mpp.widget.style.view.Margined
     * @property background widget's background, might be null if not required
     */
    data class HeaderStyle(
        val size: WidgetSize = WidgetSize(),
        val textStyle: TextStyle = TextStyle(),
        override val margins: MarginValues = MarginValues(),
        val underlineColor: Int = 0xFF000000.toInt(),
        val background: Background? = null
    ) : Margined
}
