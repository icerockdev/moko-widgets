/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.collection

import cocoapods.mokoWidgetsCollection.ALCollectionFlowLayout
import dev.icerock.moko.units.UnitCollectionViewDataSource
import dev.icerock.moko.units.createUnitCollectionViewDataSource
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.toEdgeInsets
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UICollectionView
import platform.UIKit.UICollectionViewScrollDirection
import platform.UIKit.UIColor
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIEdgeInsetsZero
import platform.UIKit.backgroundColor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class SimpleCollectionViewFactory actual constructor(
    private val orientation: Orientation,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<Fill.Solid>?
) : ViewFactory<CollectionWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: CollectionWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val layoutAndDelegate = ALCollectionFlowLayout().apply {
            sectionInset = UIEdgeInsetsZero.readValue()
            minimumInteritemSpacing = 0.0
            minimumLineSpacing = 0.0
            scrollDirection = when (orientation) {
                Orientation.HORIZONTAL -> UICollectionViewScrollDirection.UICollectionViewScrollDirectionHorizontal
                Orientation.VERTICAL -> UICollectionViewScrollDirection.UICollectionViewScrollDirectionVertical
            }
        }

        val collectionView = UICollectionView(
            frame = CGRectZero.readValue(),
            collectionViewLayout = layoutAndDelegate
        ).apply {
            backgroundColor = UIColor.clearColor

            applyBackgroundIfNeeded(background)

            when (orientation) {
                Orientation.VERTICAL -> {
                    this.setAlwaysBounceHorizontal(false)
                    this.setShowsHorizontalScrollIndicator(false)
                }
                Orientation.HORIZONTAL -> {
                    this.setAlwaysBounceVertical(false)
                    this.setShowsVerticalScrollIndicator(false)
                }
            }

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
            }
        }
        val unitDataSource = createUnitCollectionViewDataSource(collectionView)

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

//    @Suppress(
//        "CONFLICTING_OVERLOADS",
//        "RETURN_TYPE_MISMATCH_ON_INHERITANCE",
//        "MANY_INTERFACES_MEMBER_NOT_IMPLEMENTED",
//        "DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES"
//    )
//    private class SpanCollectionViewLayout(
//        private val spanCount: Int,
//        private val orientation: Orientation
//    ) : UICollectionViewFlowLayout(), UICollectionViewDelegateFlowLayoutProtocol {
//        private val reusableStubs = mutableMapOf<String, UICollectionViewCell>()
//        var dataSource: UnitCollectionViewDataSource? = null
//
//        override fun collectionView(
//            collectionView: UICollectionView,
//            layout: UICollectionViewLayout,
//            sizeForItemAtIndexPath: NSIndexPath
//        ): CValue<CGSize> {
//
//            println("Request cell for indexPath: ${sizeForItemAtIndexPath.debugDescription() ?: "unknown"}")
//
//            val fixedItemSize = getFixedCellDimension(collectionView, spanCount, orientation)
//
//            val position = sizeForItemAtIndexPath.item.toInt()
//
//            val unit = dataSource!!.unitItems!![position]
//
//            println("Request stub")
//
//            val stub = getStub(collectionView, unit, sizeForItemAtIndexPath)
//            objc_sync_enter(stub)
//            stub.setTranslatesAutoresizingMaskIntoConstraints(false)
//            unit.bind(stub)
//
//            with(stub.contentView) {
//                translatesAutoresizingMaskIntoConstraints = false
//                println("Bind unit to stub")
//
//
//                when (orientation) {
//                    Orientation.VERTICAL -> {
//                        val constraint = widthAnchor.constraintEqualToConstant(fixedItemSize)
//                        constraint.setActive(true)
//                        updateConstraints()
//                        layoutSubviews()
//                        val result = CGSizeMake(fixedItemSize, wrapContentHeight(fixedItemSize))
//                        removeConstraint(constraint)
//                        return result
//                    }
//
//                    Orientation.HORIZONTAL -> {
//                        //TODO: Copy logic from vertical
//                        return CGSizeMake(wrapContentWidth(fixedItemSize), fixedItemSize)
//                    }
//                }
//            }
//
//            objc_sync_exit(stub)
//        }
//
//        private fun getStub(
//            collectionView: UICollectionView,
//            unit: CollectionUnitItem,
//            indexPath: NSIndexPath
//        ): UICollectionViewCell {
//            println("Dequeue cell - ${unit.reusableIdentifier}")
//            val exist = reusableStubs[unit.reusableIdentifier]
//            return if (exist != null) {
//                println("Already stubbed, return")
//                exist
//            } else {
//                println("Dequeue stub")
//
//                val stub = collectionView.dequeueReusableCellWithReuseIdentifier(
//                    unit.reusableIdentifier,
//                    indexPath
//                )
//                reusableStubs[unit.reusableIdentifier] = stub
//                stub
//            }
//        }
//    }
}
//
//internal fun getFixedCellDimension(
//    forCollectionView: UICollectionView,
//    spanCount: Int,
//    andOrientation: Orientation
//): CGFloat {
//    val collectionViewSize = forCollectionView.bounds.useContents { size }
//
//    val sizeExtract: (CGSize) -> CGFloat
//    val padExtract: (UIEdgeInsets) -> CGFloat
//
//    when (andOrientation) {
//        Orientation.VERTICAL -> {
//            sizeExtract = { it.width }
//            padExtract = { it.left + it.right }
//        }
//        Orientation.HORIZONTAL -> {
//            sizeExtract = { it.height }
//            padExtract = { it.top + it.bottom }
//        }
//    }
//    return (sizeExtract(collectionViewSize)
//            - forCollectionView.contentInset.useContents(padExtract)) / spanCount
//}