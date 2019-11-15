package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.utils.applySize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIScrollView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leftAnchor
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor

actual var scrollWidgetViewFactory: VFC<ScrollWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val scrollView = UIScrollView(frame = CGRectZero.readValue())
    scrollView.translatesAutoresizingMaskIntoConstraints = false

    val childView = widget.child.buildView(viewController)
    childView.translatesAutoresizingMaskIntoConstraints = false

    with(scrollView) {
        addSubview(childView)

        childView.topAnchor.constraintEqualToAnchor(topAnchor).active = true
        childView.leftAnchor.constraintEqualToAnchor(leftAnchor).active = true
        childView.widthAnchor.constraintEqualToAnchor(widthAnchor).active = true
        bottomAnchor.constraintEqualToAnchor(childView.bottomAnchor).active = true
    }

    scrollView.applySize(style.size)
}
