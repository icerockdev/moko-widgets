/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.units.UnitTableViewDataSource
import dev.icerock.moko.units.createUnitTableViewDataSource
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.bind
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

actual class SystemListViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val dividerEnabled: Boolean?,
    private val reversed: Boolean,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
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

            applyBackgroundIfNeeded(background)

            padding?.toEdgeInsets()?.also { insetsValue ->
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

            if (dividerEnabled == false) {
                separatorStyle = UITableViewCellSeparatorStyle.UITableViewCellSeparatorStyleNone
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

        return ViewBundle(
            view = tableView,
            size = size,
            margins = margins
        )
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
}
