package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.factory.SystemListViewFactory
import dev.icerock.moko.widgets.style.view.WidgetSize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.UIViewController
import platform.UIKit.UIView

actual abstract class SearchScreen<A: Args> actual constructor(
    theme: Theme,
    size: WidgetSize,
    id: ListWidget.Id
) : Screen<A>() {
    actual abstract val searchQuery: MutableLiveData<String>
    actual abstract val searchItems: LiveData<List<TableUnitItem>>

    override fun createViewController(isLightStatusBar: Boolean?): UIViewController {
        return SearchViewController(isLightStatusBar)
    }

    var listWidget = ListWidget(
        factory = theme.factory.get(
            id = id,
            category = ListWidget.DefaultCategory,
            defaultCategory = ListWidget.DefaultCategory,
            fallback = { SystemListViewFactory() }
        ),
        size = size,
        id = id,
        items = searchItems,
        onReachEnd = null,
        onRefresh = null
    )

    fun <WS : WidgetSize> build(
        widget: ListWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = null
        )
    }

}
