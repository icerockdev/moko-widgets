package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.layoutWidget
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIScrollView
import platform.UIKit.UIScrollViewKeyboardDismissMode
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.setAccessibilityLabel
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor

actual var scrollWidgetViewFactory: VFC<ScrollWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val scrollView = UIScrollView(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false
        alwaysBounceVertical = true
        keyboardDismissMode = UIScrollViewKeyboardDismissMode.UIScrollViewKeyboardDismissModeInteractive
        applyBackground(style.background)
    }

    val childView = widget.child.buildView(viewController)
    childView.translatesAutoresizingMaskIntoConstraints = false

    with(scrollView) {
        addSubview(childView)

        val edges: Edges<CGFloat> = layoutWidget(
            rootWidget = widget,
            rootView = this,
            childWidget = widget.child,
            childView = childView
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

    scrollView
}
