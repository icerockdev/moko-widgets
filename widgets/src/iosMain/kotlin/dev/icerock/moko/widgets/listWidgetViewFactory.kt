/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.units.UnitTableViewDataSource
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.setEventHandler
import dev.icerock.moko.widgets.utils.toEdgeInsets
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIRefreshControl
import platform.UIKit.UITableView
import platform.UIKit.UITableViewAutomaticDimension
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellSeparatorStyle
import platform.UIKit.UITableViewStyle
import platform.UIKit.layoutMargins
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var listWidgetViewFactory: VFC<ListWidget> = { viewController, widget ->
    val style = widget.style

    val tableView = UITableView(
        frame = CGRectZero.readValue(),
        style = UITableViewStyle.UITableViewStylePlain
    )
    val unitDataSource = UnitTableViewDataSource(tableView)

    with(tableView) {
        translatesAutoresizingMaskIntoConstraints = false
        dataSource = unitDataSource
        rowHeight = UITableViewAutomaticDimension
        estimatedRowHeight = UITableViewAutomaticDimension

        applyBackground(style.background)

        style.padding?.toEdgeInsets()?.also { insetsValue ->
            val insets = insetsValue.useContents {
                Edges(
                    top = this.top,
                    leading = this.left,
                    trailing = this.right,
                    bottom = this.bottom
                )
            }
            contentInset = UIEdgeInsetsMake(
                top = insets.top,
                bottom = insets.bottom,
                left = 0.0,
                right = 0.0
            )
            val margins = UIEdgeInsetsMake(
                top = 0.0,
                bottom = 0.0,
                left = insets.leading,
                right = insets.trailing
            )
            layoutMargins = margins
            separatorInset = margins
        }

        widget.onRefresh?.let { onRefresh ->
            refreshControl = UIRefreshControl().apply {
                setEventHandler(UIControlEventValueChanged) {
                    onRefresh { endRefreshing() }
                }
            }
        }

        // TODO reversed apply
    }

    widget.items.bind { items ->
        unitDataSource.unitItems = if (widget.onReachEnd != null) {
            items.observedEnd(widget.onReachEnd)
        } else {
            items
        }
    }

    tableView
}

private fun List<TableUnitItem>.observedEnd(onReachEnd: () -> Unit): List<TableUnitItem> {
    if (this.isEmpty()) return this

    val lastWrapped = TableUnitItemWrapper(
        item = this.last(),
        onBind = onReachEnd
    )
    return this.dropLast(1).plus(lastWrapped)
}

private class TableUnitItemWrapper(
    private val item: TableUnitItem,
    private val onBind: () -> Unit
) : TableUnitItem by item {
    override fun bind(cell: UITableViewCell) {
        item.bind(cell)
        onBind()
    }
}
