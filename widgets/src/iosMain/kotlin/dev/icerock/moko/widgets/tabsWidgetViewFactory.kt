/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.localized
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISegmentedControl
import platform.UIKit.UISegmentedControlStyle
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.hidden
import platform.UIKit.leadingAnchor
import platform.UIKit.safeAreaLayoutGuide
import platform.UIKit.subviews
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var tabsWidgetViewFactory: VFC<TabsWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val segmentedControl = UISegmentedControl(frame = CGRectZero.readValue())
    segmentedControl.translatesAutoresizingMaskIntoConstraints = false
    segmentedControl.segmentedControlStyle = UISegmentedControlStyle.UISegmentedControlStylePlain

    val container = UIView(frame = CGRectZero.readValue())
    container.translatesAutoresizingMaskIntoConstraints = false

    widget.tabs.forEachIndexed { index, tabWidget ->
        val title = tabWidget.title
        val titleStr = title.value.localized()

        segmentedControl.insertSegmentWithTitle(
            title = titleStr,
            atIndex = index.toULong(),
            animated = false
        )

        title.bind { text ->
            segmentedControl.setTitle(
                title = text.localized(),
                forSegmentAtIndex = index.toULong()
            )
        }

        val view = tabWidget.body.buildView(viewController)
        view.translatesAutoresizingMaskIntoConstraints = false

        with(container) {
            addSubview(view)

            view.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
            view.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
            view.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
            view.topAnchor.constraintEqualToAnchor(topAnchor).active = true
        }
    }

    fun syncSelectedPage() {
        container.subviews.forEachIndexed { index, subview ->
            subview as UIView

            subview.hidden = index != segmentedControl.selectedSegmentIndex.toInt()
        }
    }

    segmentedControl.selectedSegmentIndex = 0
    syncSelectedPage()

    segmentedControl.setEventHandler(UIControlEventValueChanged) {
        syncSelectedPage()
    }

    UIView(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false

        addSubview(segmentedControl)
        addSubview(container)

        segmentedControl.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
        segmentedControl.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
        segmentedControl.topAnchor.constraintEqualToAnchor(topAnchor).active = true

        container.topAnchor.constraintEqualToAnchor(segmentedControl.bottomAnchor).active = true

        container.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
        container.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
        container.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
    }
}
