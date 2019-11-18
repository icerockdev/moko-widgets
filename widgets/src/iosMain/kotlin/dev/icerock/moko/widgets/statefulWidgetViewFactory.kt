/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.State
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackground
import dev.icerock.moko.widgets.utils.fillChildView
import dev.icerock.moko.widgets.utils.layoutWidget
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.hidden
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var statefulWidgetViewFactory: VFC<StatefulWidget<*, *>> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val container = UIView(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false
        applyBackground(style.background)
    }

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

            val edges: Edges<CGFloat> = layoutWidget(
                rootWidget = widget,
                rootView = container,
                childWidget = childWidget,
                childView = childView
            )

            fillChildView(childView, edges)
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

    container
}
