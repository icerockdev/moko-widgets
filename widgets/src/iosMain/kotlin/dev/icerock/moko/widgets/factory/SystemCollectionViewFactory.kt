/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.units.UnitCollectionViewDataSource
import dev.icerock.moko.widgets.CollectionWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.toEdgeInsets
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
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIEdgeInsetsZero
import platform.UIKit.UILayoutPriorityDefaultLow
import platform.UIKit.UILayoutPriorityRequired
import platform.UIKit.backgroundColor
import platform.UIKit.layoutIfNeeded
import platform.UIKit.row
import platform.UIKit.setNeedsLayout
import platform.UIKit.systemLayoutSizeFittingSize
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SystemCollectionViewFactory actual constructor(
    private val orientation: Orientation,
    private val spanCount: Int,
    private val background: Background?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<CollectionWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: CollectionWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val layoutAndDelegate = SpanCollectionViewLayout(spanCount).apply {
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

            applyBackgroundIfNeeded(background)

            padding?.toEdgeInsets()?.also { insetsValue ->
                val insets = insetsValue.useContents {
                    Edges(
                        top = this.top,
                        leading = this.left,
                        trailing = this.right,
                        bottom = this.bottom
                    )
                }
                contentInset = UIEdgeInsetsMake(
                    top = insets.top,
                    bottom = insets.bottom,
                    left = insets.leading,
                    right = insets.trailing
                )
//            val margins = UIEdgeInsetsMake(
//                top = 0.0,
//                bottom = 0.0,
//                left = insets.leading,
//                right = insets.trailing
//            )
//            layoutMargins = margins
            }

            // TODO add orientation support
        }
        val unitDataSource = UnitCollectionViewDataSource(collectionView)
        layoutAndDelegate.dataSource = unitDataSource

        with(collectionView) {
            translatesAutoresizingMaskIntoConstraints = false
            dataSource = unitDataSource
        }

        widget.items.bind { unitDataSource.unitItems = it }

        return ViewBundle(
            view = collectionView,
            size = size,
            margins = margins
        )
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
//        println("size: $sizeForItemAtIndexPath")
            val collectionViewSize = collectionView.bounds.useContents { size }
            val width = (collectionViewSize.width - collectionView.contentInset.useContents {
                this.left + this.right
            }) / spanCount
            val position = sizeForItemAtIndexPath.row.toInt()

//        println("width: $width")

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

//        println("sized: $rw $rh")

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
                val stub = collectionView.dequeueReusableCellWithReuseIdentifier(
                    unit.reusableIdentifier,
                    indexPath
                )
                reusableStubs[unit.reusableIdentifier] = stub
                stub
            }
        }
    }
}
