/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.units.UnitCollectionViewDataSource
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.utils.applySize
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectZero
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSIndexPath
import platform.UIKit.UICollectionView
import platform.UIKit.UICollectionViewCell
import platform.UIKit.UICollectionViewDelegateFlowLayoutProtocol
import platform.UIKit.UICollectionViewFlowLayout
import platform.UIKit.UICollectionViewLayout
import platform.UIKit.UIColor
import platform.UIKit.UIEdgeInsetsZero
import platform.UIKit.UILayoutPriorityDefaultLow
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.backgroundColor
import platform.UIKit.layoutIfNeeded
import platform.UIKit.row
import platform.UIKit.setNeedsLayout
import platform.UIKit.systemLayoutSizeFittingSize
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var collectionWidgetViewFactory: VFC<CollectionWidget> = { _, widget ->
    // TODO add styles support
    val style = widget.style

    val layoutAndDelegate = SpanCollectionViewLayout(style.spanCount).apply {
        sectionInset = UIEdgeInsetsZero.readValue()
        minimumInteritemSpacing = 0.0
        minimumLineSpacing = 0.0
    }
    val collectionView = UICollectionView(
        frame = CGRectZero.readValue(),
        collectionViewLayout = layoutAndDelegate
    ).apply {
        backgroundColor = UIColor.clearColor
        delegate = layoutAndDelegate
    }
    val unitDataSource = UnitCollectionViewDataSource(collectionView)
    layoutAndDelegate.dataSource = unitDataSource

    with(collectionView) {
        translatesAutoresizingMaskIntoConstraints = false
        dataSource = unitDataSource
    }

    widget.items.bind { unitDataSource.unitItems = it }

    collectionView.applySize(style.size)
}

private class SpanCollectionViewLayout(
    private val spanCount: Int
) : UICollectionViewFlowLayout(), UICollectionViewDelegateFlowLayoutProtocol {
    private val reusableStubs = mutableMapOf<String, UICollectionViewCell>()
    var dataSource: UnitCollectionViewDataSource? = null

    override fun collectionView(
        collectionView: UICollectionView,
        layout: UICollectionViewLayout,
        sizeForItemAtIndexPath: NSIndexPath
    ): CValue<CGSize> {
        println("size: $sizeForItemAtIndexPath")
        val collectionViewSize = collectionView.bounds.useContents { size }
        val width = collectionViewSize.width / spanCount
        val position = sizeForItemAtIndexPath.row.toInt()

        println("width: $width")

        val unit = dataSource!!.unitItems!![position]
        // TODO create correct cell from unit
        val stub = UICollectionViewCell()//getStub(collectionView, unit, sizeForItemAtIndexPath)

        val size = with(stub.contentView) {
            translatesAutoresizingMaskIntoConstraints = false
            unit.bind(stub)

            setNeedsLayout()
            layoutIfNeeded()

            systemLayoutSizeFittingSize(
                CGSizeMake(width, 0.0),
                UILayoutPriorityRequired,
                UILayoutPriorityDefaultLow
            )
        }

        val (rw, rh) = size.useContents { width to height }

        println("sized: $rw $rh")

        return CGSizeMake(rw, rh)
    }

    private fun getStub(
        collectionView: UICollectionView,
        unit: CollectionUnitItem,
        indexPath: NSIndexPath
    ): UICollectionViewCell {
        val exist = reusableStubs[unit.reusableIdentifier]
        return if (exist != null) exist
        else {
            val stub = collectionView.dequeueReusableCellWithReuseIdentifier(unit.reusableIdentifier, indexPath)
            reusableStubs[unit.reusableIdentifier] = stub
            stub
        }
    }
}
