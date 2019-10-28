package dev.icerock.moko.widgets.old

import com.icerockdev.mpp.widgets.style.view.PaddingValues
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var linearListWidgetViewFactory: VFC<LinearListWidget>

class LinearListWidget(
    private val factory: VFC<LinearListWidget> = linearListWidgetViewFactory,
    val source: LiveData<List<UnitItem>>,
    val style: Style,
    val onReachEnd: (() -> Unit)?,
    val onRefresh: (() -> Unit)?
) : Widget() {
    override fun buildView(viewFactoryContext: ViewFactoryContext): View = factory(viewFactoryContext, this)

    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    data class Style(
        val orientation: Orientation = Orientation.VERTICAL,
        val reversed: Boolean = false,
        val paddingValues: PaddingValues = PaddingValues()
    )
}
