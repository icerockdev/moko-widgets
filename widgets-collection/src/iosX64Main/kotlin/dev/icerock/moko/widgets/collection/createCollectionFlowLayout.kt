/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.collection

import cocoapods.mokoWidgetsCollection.ALCollectionFlowLayout
import platform.UIKit.UICollectionViewFlowLayout

actual fun createCollectionFlowLayout(): UICollectionViewFlowLayout {
    return ALCollectionFlowLayout()
}
