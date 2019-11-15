/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.utils.applySize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.leftAnchor
import platform.UIKit.rightAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var linearWidgetViewFactory: VFC<LinearWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val container = UIView(frame = CGRectZero.readValue())
    container.translatesAutoresizingMaskIntoConstraints = false

    var lastChildView: UIView? = null
    widget.childs.forEach { widget ->
        val childView = widget.buildView(viewController)
        childView.translatesAutoresizingMaskIntoConstraints = false

        with(container) {
            container.addSubview(childView)

            val lastCV = lastChildView
            when(style.orientation) {
                Orientation.VERTICAL -> {
                    if (lastCV != null) {
                        childView.topAnchor.constraintEqualToAnchor(lastCV.bottomAnchor).active = true
                    } else {
                        childView.topAnchor.constraintEqualToAnchor(topAnchor).active = true
                    }
                    childView.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
                    childView.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
                }
                Orientation.HORIZONTAL -> {
                    if (lastCV != null) {
                        childView.leftAnchor.constraintEqualToAnchor(lastCV.rightAnchor).active = true
                    } else {
                        childView.leftAnchor.constraintEqualToAnchor(leftAnchor).active = true
                    }
                    childView.topAnchor.constraintEqualToAnchor(topAnchor).active = true
                    childView.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
                }
            }
        }

        lastChildView = childView
    }

    lastChildView?.run {
        when(style.orientation) {
            Orientation.VERTICAL -> container.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
            Orientation.HORIZONTAL -> container.rightAnchor.constraintEqualToAnchor(rightAnchor).active = true
        }
    }

    container.applySize(style.size)
}
