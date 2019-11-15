/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applySize
import platform.UIKit.NSLayoutDimension
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.heightAnchor
import platform.UIKit.leftAnchor
import platform.UIKit.rightAnchor
import platform.UIKit.superview
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor

actual var containerWidgetViewFactory: VFC<ContainerWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val root = UIView()
    root.translatesAutoresizingMaskIntoConstraints = false

    widget.childs.forEach { (childWidget, childAlignment) ->
        val childView = childWidget.buildView(viewController)
        childView.translatesAutoresizingMaskIntoConstraints = false

        root.addSubview(childView)

        when (childAlignment) {
            Alignment.CENTER -> {
                childView.centerXAnchor.constraintEqualToAnchor(root.centerXAnchor).active = true
                childView.centerYAnchor.constraintEqualToAnchor(root.centerYAnchor).active = true
            }
            Alignment.LEFT -> {
                childView.leftAnchor.constraintEqualToAnchor(root.leftAnchor).active = true
            }
            Alignment.RIGHT -> {
                childView.rightAnchor.constraintEqualToAnchor(root.rightAnchor).active = true
            }
            Alignment.TOP -> {
                childView.topAnchor.constraintEqualToAnchor(root.topAnchor).active = true
            }
            Alignment.BOTTOM -> {
                childView.bottomAnchor.constraintEqualToAnchor(root.bottomAnchor).active = true
            }
        }
    }

    root.applySize(style.size)
}
