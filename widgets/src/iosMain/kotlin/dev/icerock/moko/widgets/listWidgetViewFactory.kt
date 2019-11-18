/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.units.UnitTableViewDataSource
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.toEdgeInsets
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UITableView
import platform.UIKit.UITableViewAutomaticDimension
import platform.UIKit.UITableViewStyle
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var listWidgetViewFactory: VFC<ListWidget> = { viewController, widget ->
    // TODO add styles support
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

        style.padding?.toEdgeInsets()?.also {
            contentInset = it
        }
    }

    widget.items.bind { unitDataSource.unitItems = it }

    tableView
}
