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
//        println("size: $sizeForItemAtIndexPath")
            val collectionViewSize = collectionView.bounds.useContents { size }

            val sizeExtract: (CGSize) -> CGFloat
            val padExtract: (UIEdgeInsets) -> CGFloat

            when (orientation) {
                Orientation.VERTICAL -> {
                    sizeExtract = { it.width }
                    padExtract = { it.left + it.right }
                }
                Orientation.HORIZONTAL -> {
                    sizeExtract = { it.height }
                    padExtract = { it.top + it.bottom }
                }
            }

            val fixedItemSize = (sizeExtract(collectionViewSize)
                    - collectionView.contentInset.useContents(padExtract)) / spanCount

            val position = sizeForItemAtIndexPath.item.toInt()

//        println("width: $width")

            val unit = dataSource!!.unitItems!![position]

            val stub = getStub(collectionView, unit, sizeForItemAtIndexPath)
            with(stub.contentView) {

                translatesAutoresizingMaskIntoConstraints = false
                unit.bind(stub)

                return when (orientation) {
                    Orientation.VERTICAL ->
                        CGSizeMake(wrapContentHeight(fixedItemSize), fixedItemSize)

                    Orientation.HORIZONTAL ->
                        CGSizeMake(fixedItemSize, wrapContentWidth(fixedItemSize))
                }
            }
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
