/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Widget
import platform.UIKit.UITableView
import platform.UIKit.UITableViewCell

actual abstract class WidgetsTableUnitItem<T> actual constructor(
    override val itemId: Long,
    val data: T
) : TableUnitItem {
    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): Widget

    override val reusableIdentifier: String get() = reuseId

    override fun register(intoView: UITableView) {
        intoView.registerClass(
            cellClass = UITableViewCell().`class`(),
            forCellReuseIdentifier = reusableIdentifier
        )
    }

    override fun bind(cell: UITableViewCell) {
        cell.contentView.setupWidgetContent(data, ::createWidget)
    }
}
