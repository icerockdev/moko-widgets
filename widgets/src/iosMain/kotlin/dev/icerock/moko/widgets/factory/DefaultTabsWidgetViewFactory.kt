/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.fillChildView
import dev.icerock.moko.widgets.utils.applySizeToChild
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UILayoutPriorityDefaultLow
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.UISegmentedControl
import platform.UIKit.UISegmentedControlStyle
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.hidden
import platform.UIKit.leadingAnchor
import platform.UIKit.setContentHuggingPriority
import platform.UIKit.subviews
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class DefaultTabsWidgetViewFactory actual constructor(
    style: Style
) : DefaultTabsWidgetViewFactoryBase(style) {

    @ExperimentalUnsignedTypes
    override fun <WS : WidgetSize> build(
        widget: TabsWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val segmentedControl = UISegmentedControl(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            segmentedControlStyle = UISegmentedControlStyle.UISegmentedControlStylePlain
            setContentHuggingPriority(
                UILayoutPriorityDefaultLow,
                forAxis = UILayoutConstraintAxisVertical
            )
        }

        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            setContentHuggingPriority(
                UILayoutPriorityRequired,
                forAxis = UILayoutConstraintAxisVertical
            )
        }

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

            val viewBundle = tabWidget.body.buildView(viewController)
            val view = viewBundle.view
            view.translatesAutoresizingMaskIntoConstraints = false

            with(container) {
                addSubview(view)

                val edges: Edges<CGFloat> = applySizeToChild(
                    rootView = container,
                    rootPadding = null,
                    childView = view,
                    childSize = viewBundle.size,
                    childMargins = viewBundle.margins
                )

                fillChildView(view, edges)
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

        val view = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackground(style.background)

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

        return ViewBundle(
            view = view,
            size = size,
            margins = style.margins
        )
    }
}
