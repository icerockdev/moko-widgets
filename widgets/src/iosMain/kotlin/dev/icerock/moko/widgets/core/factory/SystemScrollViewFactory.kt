/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applySizeToChild
import dev.icerock.moko.widgets.core.widget.ScrollWidget
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIScrollView
import platform.UIKit.UIScrollViewKeyboardDismissMode
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor

actual class SystemScrollViewFactory actual constructor(
    private val background: Background<Fill.Solid>?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<ScrollWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ScrollWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val scrollView = UIScrollView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            alwaysBounceVertical = true
            keyboardDismissMode = UIScrollViewKeyboardDismissMode.UIScrollViewKeyboardDismissModeInteractive
            applyBackgroundIfNeeded(background)
        }

        val childBundle = widget.child.buildView(viewController)
        val childView = childBundle.view
        childView.translatesAutoresizingMaskIntoConstraints = false

        with(scrollView) {
            addSubview(childView)

            val edges: Edges<CGFloat> =
                applySizeToChild(
                    rootView = this,
                    rootPadding = padding,
                    childView = childView,
                    childSize = childBundle.size,
                    childMargins = childBundle.margins
                )

            childView.topAnchor.constraintEqualToAnchor(
                anchor = topAnchor,
                constant = edges.top
            ).active = true
            childView.leadingAnchor.constraintEqualToAnchor(
                anchor = leadingAnchor,
                constant = edges.leading
            ).active = true
            childView.widthAnchor.constraintEqualToAnchor(
                anchor = widthAnchor,
                constant = -(edges.trailing + edges.leading)
            ).active = true
            contentLayoutGuide.widthAnchor.constraintEqualToAnchor(
                anchor = childView.widthAnchor
            ).active = true
            contentLayoutGuide.bottomAnchor.constraintEqualToAnchor(
                anchor = childView.bottomAnchor,
                constant = edges.bottom
            ).active = true
        }

        return ViewBundle(
            view = scrollView,
            size = size,
            margins = margins
        )
    }
}
