/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applySize
import dev.icerock.moko.widgets.core.objc.getAssociatedObject
import dev.icerock.moko.widgets.core.objc.setAssociatedObject
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication
import platform.UIKit.UILayoutPriorityDefaultHigh
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.layoutMargins
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

private fun <T> UIView.getWidgetLiveData(): MutableLiveData<T>? {
    return getAssociatedObject(this) as? MutableLiveData<T>
}

private fun <T> UIView.setWidgetLiveData(liveData: MutableLiveData<T>) {
    setAssociatedObject(this, liveData)
}

internal fun <T> UIView.setupWidgetContent(
    data: T,
    factory: (liveData: LiveData<T>) -> Widget<out WidgetSize>
) {
    val liveData = this.getWidgetLiveData<T>()
    if (liveData != null) {
        liveData.value = data
    } else {
        // TODO get correct viewcontroller for widgets
        val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController!!
        val mutableLiveData = MutableLiveData(initialValue = data)
        val widget = factory(mutableLiveData)
        val viewBundle = widget.buildView(viewFactoryContext = viewController)
        val view = viewBundle.view.apply {
            translatesAutoresizingMaskIntoConstraints = false
        }

        addSubview(view)

        val (margin_left, margin_right) = layoutMargins.useContents { left to right }

        val childSize = viewBundle.size
        val childMargins = viewBundle.margins

        val edges = Edges(
            top = childMargins?.top?.toDouble() ?: 0.0, //TODO: Support this
            leading = childMargins?.start?.toDouble() ?: 0.0, // + margin_left,
            bottom = childMargins?.bottom?.toDouble() ?: 0.0, //TODO: Support this
            trailing = childMargins?.end?.toDouble() ?: 0.0 //+ margin_right
        )
        view.applySize(childSize, this, edges)

        view.topAnchor.constraintEqualToAnchor(
            anchor = topAnchor,
            constant = edges.top
        ).active = true

        view.leadingAnchor.constraintEqualToAnchor(
            anchor = leadingAnchor,
            constant = edges.leading
        ).active = true

        trailingAnchor.constraintEqualToAnchor(
            anchor = view.trailingAnchor,
            constant = edges.trailing
        ).apply {
            priority = UILayoutPriorityDefaultHigh
            active = true
        }

        bottomAnchor.constraintEqualToAnchor(
            anchor = view.bottomAnchor,
            constant = edges.bottom
        ).apply {
            priority = UILayoutPriorityDefaultHigh
            active = true
        }

        this.setWidgetLiveData(mutableLiveData)
    }
}
