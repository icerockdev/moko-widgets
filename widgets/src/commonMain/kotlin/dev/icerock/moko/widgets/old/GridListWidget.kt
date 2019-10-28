package dev.icerock.moko.widgets.old

import com.icerockdev.mpp.widgets.style.view.PaddingValues
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget

expect var gridListWidgetViewFactory: VFC<GridListWidget>

class GridListWidget(
    private val factory: VFC<GridListWidget>,
    val items: LiveData<List<UnitItem>>,
    val style: Style,
    val onReachEnd: (() -> Unit)?,
    val onRefresh: (() -> Unit)?
) : Widget() {

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)

    enum class Orientation {
        VERTICAL,
        HORIZONTAL
    }

    data class Style(
        val spanCount: Int = 2,
        val orientation: Orientation = Orientation.VERTICAL,
        val paddingValues: PaddingValues = PaddingValues()
    )
}
