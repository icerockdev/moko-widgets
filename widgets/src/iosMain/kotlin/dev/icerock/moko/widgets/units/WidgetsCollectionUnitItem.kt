/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.units

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.utils.applySize
import dev.icerock.moko.widgets.utils.getMargins
import dev.icerock.moko.widgets.utils.getSize
import dev.icerock.plural.getAssociatedObject
import dev.icerock.plural.setAssociatedObject
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication
import platform.UIKit.UICollectionView
import platform.UIKit.UICollectionViewCell
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.layoutMargins
import platform.UIKit.layoutMarginsGuide
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual abstract class WidgetsCollectionUnitItem<T> actual constructor(
    override val itemId: Long,
    private val data: T
) : CollectionUnitItem {
    actual abstract val reuseId: String
    actual abstract fun createWidget(data: LiveData<T>): Widget

    override val reusableIdentifier: String get() = reuseId

    override fun register(intoView: UICollectionView) {
        intoView.registerClass(
            cellClass = UICollectionViewCell().`class`(),
            forCellWithReuseIdentifier = reusableIdentifier
        )
    }

    override fun bind(cell: UICollectionViewCell) {
        cell.contentView.setupWidgetContent(data, ::createWidget)
    }
}

private fun <T> UIView.getWidgetLiveData(): MutableLiveData<T>? {
    return getAssociatedObject(this) as? MutableLiveData<T>
}

private fun <T> UIView.setWidgetLiveData(liveData: MutableLiveData<T>) {
    setAssociatedObject(this, liveData)
}

internal fun <T> UIView.setupWidgetContent(data: T, factory: (liveData: LiveData<T>) -> Widget) {
    val liveData = this.getWidgetLiveData<T>()
    if (liveData != null) {
        liveData.value = data
    } else {
        val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController!!
        val mutableLiveData = MutableLiveData(initialValue = data)
        val widget = factory(mutableLiveData)
        val view = widget.buildView(viewFactoryContext = viewController).apply {
            translatesAutoresizingMaskIntoConstraints = false
        }

        addSubview(view)

        val (margin_left, margin_right) = layoutMargins.useContents { left to right }
        val childSize = widget.getSize()
        val childMargins = widget.getMargins()
        val edges = dev.icerock.moko.widgets.utils.Edges(
            top = childMargins?.top?.toDouble() ?: 0.0,
            leading = childMargins?.start?.toDouble() ?: 0.0 + margin_left,
            bottom = childMargins?.bottom?.toDouble() ?: 0.0,
            trailing = childMargins?.end?.toDouble() ?: 0.0 + margin_right
        )

        if (childSize != null) {
            view.applySize(childSize, this, edges)
        }

        view.topAnchor.constraintEqualToAnchor(
            anchor = topAnchor,
            constant = edges.top
        ).active = true
        view.leadingAnchor.constraintEqualToAnchor(
            anchor = layoutMarginsGuide.leadingAnchor,
            constant = edges.leading
        ).active = true
        view.trailingAnchor.constraintEqualToAnchor(
            anchor = layoutMarginsGuide.trailingAnchor,
            constant = edges.trailing
        ).active = true
        view.bottomAnchor.constraintEqualToAnchor(
            anchor = bottomAnchor,
            constant = edges.bottom
        ).active = true

        this.setWidgetLiveData(mutableLiveData)
    }
}