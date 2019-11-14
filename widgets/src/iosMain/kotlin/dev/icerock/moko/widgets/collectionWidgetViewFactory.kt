/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.units.UnitCollectionViewDataSource
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UICollectionView
import platform.UIKit.UICollectionViewLayout
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var collectionWidgetViewFactory: VFC<CollectionWidget> = { _, widget ->
    // TODO add styles support
    val style = widget.style

    val collectionView = UICollectionView(
        frame = CGRectZero.readValue(),
        collectionViewLayout = UICollectionViewLayout()
    )
    val unitDataSource = UnitCollectionViewDataSource(collectionView)

    with(collectionView) {
        translatesAutoresizingMaskIntoConstraints = false
        dataSource = unitDataSource
    }

    widget.items.bind { unitDataSource.unitItems = it }

    collectionView
}