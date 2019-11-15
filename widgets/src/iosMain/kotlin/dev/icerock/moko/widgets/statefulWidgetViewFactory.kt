/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.utils.applySize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.hidden
import platform.UIKit.leftAnchor
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor

actual var statefulWidgetViewFactory: VFC<StatefulWidget<*, *>> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val container = UIView(frame = CGRectZero.readValue())
    container.translatesAutoresizingMaskIntoConstraints = false

    listOf<Widget>(
        widget.dataWidget,
        widget.emptyWidget,
        widget.loadingWidget,
        widget.errorWidget
    ).forEach { childWidget ->
        val childView = childWidget.buildView(viewController)
        childView.translatesAutoresizingMaskIntoConstraints = false

        with(container) {
            addSubview(childView)

            childView.topAnchor.constraintEqualToAnchor(topAnchor).active = true
            childView.leftAnchor.constraintEqualToAnchor(leftAnchor).active = true
            childView.widthAnchor.constraintEqualToAnchor(widthAnchor).active = true
            childView.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
        }

        fun updateState(state: State<*, *>) {
            childView.hidden = when (state) {
                is State.Data -> childWidget == widget.dataWidget
                is State.Empty -> childWidget == widget.emptyWidget
                is State.Error -> childWidget == widget.errorWidget
                is State.Loading -> childWidget == widget.loadingWidget
            }.not()
        }

        updateState(widget.stateLiveData.value)
        widget.stateLiveData.addObserver { updateState(it) }
    }

    container.applySize(style.size)
}
