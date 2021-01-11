/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.units.createUnitTableViewDataSource
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.widget.ListWidget
import dev.icerock.moko.widgets.core.widget.ScrollListView
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGAffineTransformMakeRotation
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UIRefreshControl
import platform.UIKit.UITableView
import platform.UIKit.UITableViewAutomaticDimension
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellSeparatorStyle
import platform.UIKit.UITableViewStyle
import platform.UIKit.translatesAutoresizingMaskIntoConstraints


actual class ReversedListWidgetFactory actual constructor(
    private val background: Background<Fill.Solid>?
) : ViewFactory<ListWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ListWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val tableView = UITableView(
            frame = CGRectZero.readValue(),
            style = UITableViewStyle.UITableViewStylePlain
        )
        val unitDataSource = createUnitTableViewDataSource(tableView)

        with(tableView) {
            translatesAutoresizingMaskIntoConstraints = false
            dataSource = unitDataSource
            rowHeight = UITableViewAutomaticDimension
            estimatedRowHeight = UITableViewAutomaticDimension
            allowsSelection = false
            separatorStyle = UITableViewCellSeparatorStyle.UITableViewCellSeparatorStyleNone

            // reverse options
            transform = CGAffineTransformMakeRotation(angle = -kotlin.math.PI)
            showsVerticalScrollIndicator = false

            applyBackgroundIfNeeded(background)

            widget.onRefresh?.let { onRefresh ->
                refreshControl = UIRefreshControl().apply {
                    setEventHandler(UIControlEventValueChanged) {
                        onRefresh { endRefreshing() }
                    }
                }
            }
        }

        widget.lastScrollView = object : ScrollListView {
            override fun scrollToFirstItem() {
                // TODO: add scroll to first item
            }
        }

        widget.items.bind { items ->
            val lastIndex = items.lastIndex
            val processedItems = items.mapIndexed { index: Int, tableUnitItem: TableUnitItem ->
                TableUnitItemWrapper(
                    item = tableUnitItem,
                    onBind = widget.onReachEnd?.takeIf { index == lastIndex }
                )
            }
            unitDataSource.unitItems = processedItems
        }

        return ViewBundle(
            view = tableView,
            size = size,
            margins = null
        )
    }

    private class TableUnitItemWrapper(
        private val item: TableUnitItem,
        private val onBind: (() -> Unit)? = null
    ) : TableUnitItem by item {
        override fun bind(tableViewCell: UITableViewCell) {
            item.bind(tableViewCell)
            onBind?.invoke()

            // reverse options
            tableViewCell.transform = CGAffineTransformMakeRotation(angle = kotlin.math.PI)
        }
    }
}
