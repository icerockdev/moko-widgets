/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.layoutWidget
import dev.icerock.moko.widgets.utils.setHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIAccessibilityIdentificationProtocol
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UIView
import platform.UIKit.addGestureRecognizer
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var containerWidgetViewFactory: VFC<ContainerWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val root = UIViewWithIdentifier().apply {
        translatesAutoresizingMaskIntoConstraints = false
        applyBackground(style.background)

        accessibilityIdentifier = widget.identifier()

//        if (widget.onTap != null) {
//            val tapGestureRecognizer = UITapGestureRecognizer()
//            tapGestureRecognizer.setHandler { widget.onTap.invoke() }
//
//            addGestureRecognizer(tapGestureRecognizer)
//        }
    }

    widget.children.forEach { (childWidget, childAlignment) ->
        val childView = childWidget.buildView(viewController)
        childView.translatesAutoresizingMaskIntoConstraints = false

        root.addSubview(childView)

        val edges: Edges<CGFloat> = layoutWidget(
            rootWidget = widget,
            rootView = root,
            childWidget = childWidget,
            childView = childView
        )

        when (childAlignment) {
            Alignment.CENTER -> {
                childView.centerXAnchor.constraintEqualToAnchor(root.centerXAnchor).active = true
                childView.centerYAnchor.constraintEqualToAnchor(root.centerYAnchor).active = true
            }
            Alignment.LEFT -> {
                childView.leadingAnchor.constraintEqualToAnchor(
                    anchor = root.leadingAnchor,
                    constant = edges.leading
                ).active = true
                childView.centerYAnchor.constraintEqualToAnchor(root.centerYAnchor).active = true
            }
            Alignment.RIGHT -> {
                childView.trailingAnchor.constraintEqualToAnchor(
                    anchor = root.trailingAnchor,
                    constant = edges.trailing
                ).active = true
                childView.centerYAnchor.constraintEqualToAnchor(root.centerYAnchor).active = true
            }
            Alignment.TOP -> {
                childView.topAnchor.constraintEqualToAnchor(
                    anchor = root.topAnchor,
                    constant = edges.top
                ).active = true
                childView.centerXAnchor.constraintEqualToAnchor(root.centerXAnchor).active = true
            }
            Alignment.BOTTOM -> {
                childView.bottomAnchor.constraintEqualToAnchor(
                    anchor = root.bottomAnchor,
                    constant = edges.bottom
                ).active = true
                childView.centerXAnchor.constraintEqualToAnchor(root.centerXAnchor).active = true
            }
        }
    }

    root
}

class UIViewWithIdentifier : UIView(frame = CGRectZero.readValue()),
    UIAccessibilityIdentificationProtocol {
    private var _accessibilityIdentifier: String? = null

    override fun accessibilityIdentifier(): String? {
        return _accessibilityIdentifier
    }

    override fun setAccessibilityIdentifier(accessibilityIdentifier: String?) {
        _accessibilityIdentifier = accessibilityIdentifier
    }
}

internal fun <T> T.identifier(): String where T : Widget, T : OptionalId<*> {
    val i = id
    return if (i != null) i::class.toString()
    else this.toString()
}
