/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.widget.ListWidget
import platform.Foundation.NSIndexPath
import platform.UIKit.UITableView
import platform.UIKit.UITableViewScrollPosition
import platform.UIKit.indexPathForRow
import platform.UIKit.subviews

actual class AutoScrollListViewFactory actual constructor(
    private val listViewFactory: ViewFactory<ListWidget<out WidgetSize>>,
    private val isAlwaysAutoScroll: Boolean
) : ViewFactory<ListWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ListWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle = listViewFactory.build(widget, size, viewFactoryContext)
        val view = bundle.view

        val tableView: UITableView = findTableView(view)
            ?: throw ClassCastException("UITableView was not found in the View hierarchy and could not be cast.")

        var isAutoScrolled = false

        widget.items.bind { units ->
            if ((!isAutoScrolled || isAlwaysAutoScroll) && units.isNotEmpty()) {
                val indexPath = NSIndexPath.indexPathForRow(
                    row = (units.size - 1).toLong(),
                    inSection = 0
                )

                tableView.scrollToRowAtIndexPath(
                    indexPath = indexPath,
                    atScrollPosition = UITableViewScrollPosition.UITableViewScrollPositionBottom,
                    animated = true
                )

                isAutoScrolled = true
            }
        }

        return bundle
    }

    private fun findTableView(view: View): UITableView? {
        return when (view) {
            is UITableView -> view
            else -> {
                view.subviews.forEach {
                    val subview = it as View

                    val tv = findTableView(subview)
                    if (tv != null) return tv
                }

                return null
            }
        }
    }
}