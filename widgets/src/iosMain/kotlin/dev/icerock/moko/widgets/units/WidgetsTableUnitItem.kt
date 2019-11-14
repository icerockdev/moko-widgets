package dev.icerock.moko.widgets.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.uikit.WidgetFactoryResult
import dev.icerock.moko.widgets.uikit.WidgetTableViewCell
import platform.UIKit.UITableView
import platform.UIKit.UITableViewCell

actual abstract class WidgetsTableUnitItem<T> actual constructor(override val itemId: Long, val data: T) :
    TableUnitItem {
    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): Widget

    override val reusableIdentifier: String get() = reuseId

    override fun register(intoView: UITableView) {
        WidgetTableViewCell.setViewFactory(factory = { viewController ->
            val mutableLiveData = MutableLiveData(initialValue = data)
            val widget = createWidget(mutableLiveData)
            val view = widget.buildView(viewFactoryContext = viewController!!)
            WidgetFactoryResult().apply {
                setView(view)
                setCellTag(mutableLiveData)
            }
        }, toReuseIdentifier = reuseId)

        intoView.registerClass(
            cellClass = WidgetTableViewCell().`class`(),
            forCellReuseIdentifier = reusableIdentifier
        )
    }

    override fun bind(cell: UITableViewCell) {
        cell as WidgetTableViewCell

        @Suppress("UNCHECKED_CAST")
        val mutableLiveData = cell.cellTag as MutableLiveData<T>

        mutableLiveData.value = data
    }
}