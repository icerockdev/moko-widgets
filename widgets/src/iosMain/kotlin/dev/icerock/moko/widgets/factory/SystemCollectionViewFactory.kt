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
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.background.Orientation
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.*

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.*
import platform.Foundation.NSIndexPath
import platform.UIKit.*
import platform.objc.objc_sync_enter
import platform.objc.objc_sync_exit

actual class SystemCollectionViewFactory actual constructor(
    private val orientation: Orientation,
    private val spanCount: Int,
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<Fill.Solid>?
) : ViewFactory<CollectionWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: CollectionWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val layoutAndDelegate = SpanCollectionViewLayout(spanCount, orientation).apply {
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



    @Suppress(
        "CONFLICTING_OVERLOADS",
        "RETURN_TYPE_MISMATCH_ON_INHERITANCE",
        "MANY_INTERFACES_MEMBER_NOT_IMPLEMENTED",
        "DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES"
    )
    private class SpanCollectionViewLayout(
        private val spanCount: Int,
        private val orientation: Orientation
    ) : UICollectionViewFlowLayout(), UICollectionViewDelegateFlowLayoutProtocol {
        private val reusableStubs = mutableMapOf<String, UICollectionViewCell>()
        var dataSource: UnitCollectionViewDataSource? = null

        override fun collectionView(
            collectionView: UICollectionView,
            layout: UICollectionViewLayout,
            sizeForItemAtIndexPath: NSIndexPath
        ): CValue<CGSize> {

            println("Request cell for indexPath: ${sizeForItemAtIndexPath.debugDescription() ?: "unknown"}")

            val fixedItemSize = getFixedCellDimension(collectionView, spanCount, orientation)

            val position = sizeForItemAtIndexPath.item.toInt()

            val unit = dataSource!!.unitItems!![position]

            println("Request stub")

            val stub = getStub(collectionView, unit, sizeForItemAtIndexPath)
            objc_sync_enter(stub)
            stub.setTranslatesAutoresizingMaskIntoConstraints(false)
            unit.bind(stub)

            val containerSubview = (stub.contentView.subviews.first() as? UIView) ?: stub.contentView

            with(containerSubview) {

                translatesAutoresizingMaskIntoConstraints = false
                println("Bind unit to stub")


                when (orientation) {
                    Orientation.VERTICAL -> {
                        widthAnchor.constraintEqualToConstant(fixedItemSize).setActive(true)
                        stub.contentView.updateConstraints()
                        stub.contentView.layoutSubviews()
                        return CGSizeMake(fixedItemSize, wrapContentHeight(fixedItemSize))
                    }

                    Orientation.HORIZONTAL -> {
                        heightAnchor.constraintEqualToConstant(fixedItemSize).setActive(true)
                        return CGSizeMake(wrapContentWidth(fixedItemSize), fixedItemSize)
                    }
                }
            }
            objc_sync_exit(stub)
        }

        private fun getStub(
            collectionView: UICollectionView,
            unit: CollectionUnitItem,
            indexPath: NSIndexPath
        ): UICollectionViewCell {
            println("Dequeue cell - ${unit.reusableIdentifier}")
            val exist = reusableStubs[unit.reusableIdentifier]
            return if (exist != null) {
                println("Already stubbed, return")
                exist
            }
            else {
                println("Dequeue stub")

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

internal fun getFixedCellDimension(forCollectionView: UICollectionView, spanCount: Int, andOrientation: Orientation): CGFloat {
    val collectionViewSize = forCollectionView.bounds.useContents { size }

    val sizeExtract: (CGSize) -> CGFloat
    val padExtract: (UIEdgeInsets) -> CGFloat

    when (andOrientation) {
        Orientation.VERTICAL -> {
            sizeExtract = { it.width }
            padExtract = { it.left + it.right }
        }
        Orientation.HORIZONTAL -> {
            sizeExtract = { it.height }
            padExtract = { it.top + it.bottom }
        }
    }
    return (sizeExtract(collectionViewSize)
            - forCollectionView.contentInset.useContents(padExtract)) / spanCount
}