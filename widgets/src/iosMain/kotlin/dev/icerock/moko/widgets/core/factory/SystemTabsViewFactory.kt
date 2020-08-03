/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.SelectableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applySizeToChild
import dev.icerock.moko.widgets.core.utils.bind
import dev.icerock.moko.widgets.core.utils.fillChildView
import dev.icerock.moko.widgets.core.utils.setEventHandler
import dev.icerock.moko.widgets.core.widget.TabsWidget
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIControlStateSelected
import platform.UIKit.UIDevice
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
import platform.UIKit.tintColor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemTabsViewFactory actual constructor(
    private val tabsTintColor: Color?,
    private val titleColor: SelectableState<Color?>?,
    private val tabsBackground: Background<Fill.Solid>?,
    private val contentBackground: Background<out Fill>?,
    private val tabsPadding: PaddingValues?,
    private val contentPadding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<TabsWidget<out WidgetSize>> {

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

            tabsTintColor?.also {
                if (UIDevice.currentDevice.systemVersion.compareTo("13.0") < 0) {
                    tintColor = it.toUIColor()
                } else {
                    selectedSegmentTintColor = it.toUIColor()
                }
            }

            applyBackgroundIfNeeded(tabsBackground)
        }

        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            setContentHuggingPriority(
                UILayoutPriorityRequired,
                forAxis = UILayoutConstraintAxisVertical
            )

            applyBackgroundIfNeeded(contentBackground)
        }

        titleColor?.also { stateColor ->
            stateColor.selected?.also {
                segmentedControl.setTitleTextAttributes(
                    attributes = mapOf(
                        NSForegroundColorAttributeName to it.toUIColor()
                    ),
                    forState = UIControlStateSelected
                )
            }
            stateColor.unselected?.also {
                segmentedControl.setTitleTextAttributes(
                    attributes = mapOf(
                        NSForegroundColorAttributeName to it.toUIColor()
                    ),
                    forState = UIControlStateNormal
                )
            }
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

                val edges: Edges<CGFloat> =
                    applySizeToChild(
                        rootView = container,
                        rootPadding = contentPadding,
                        childView = view,
                        childSize = viewBundle.size,
                        childMargins = viewBundle.margins
                    )

                fillChildView(view, edges)
            }

            widget.selectedTab?.addObserver { tabIndex ->
                if (segmentedControl.selectedSegmentIndex.toInt() != tabIndex) {
                    segmentedControl.setSelectedSegmentIndex(tabIndex.toLong())
                }
            }
        }

        fun syncSelectedPage() {
            container.subviews.forEachIndexed { index, subview ->
                subview as UIView

                subview.hidden = index != segmentedControl.selectedSegmentIndex.toInt()
            }
            widget.selectedTab?.postValue(segmentedControl.selectedSegmentIndex.toInt())
        }

        segmentedControl.selectedSegmentIndex = 0
        syncSelectedPage()

        segmentedControl.setEventHandler(UIControlEventValueChanged) {
            syncSelectedPage()
        }

        val view = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false

            addSubview(segmentedControl)
            addSubview(container)

            segmentedControl.leadingAnchor.constraintEqualToAnchor(
                leadingAnchor,
                constant = tabsPadding?.start?.toDouble() ?: 0.0
            ).active = true
            trailingAnchor.constraintEqualToAnchor(
                segmentedControl.trailingAnchor,
                constant = tabsPadding?.end?.toDouble() ?: 0.0
            ).active = true
            segmentedControl.topAnchor.constraintEqualToAnchor(
                topAnchor,
                constant = tabsPadding?.top?.toDouble() ?: 0.0
            ).active = true

            container.topAnchor.constraintEqualToAnchor(
                segmentedControl.bottomAnchor,
                constant = tabsPadding?.bottom?.toDouble() ?: 0.0
            ).active = true

            container.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
            container.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
            container.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
        }
        
        return ViewBundle(
            view = view,
            size = size,
            margins = margins
        )
    }
}
