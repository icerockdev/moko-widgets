/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.imagenetwork

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.Image
import platform.UIKit.UIImage
import platform.UIKit.UIView
import cocoapods.mokoWidgetsImageNetwork.ImageNetwork

actual fun Image.Companion.network(
    url: String,
    placeholder: ImageResource?
): Image {
    return object : Image() {
        override fun apply(view: UIView, block: (UIImage?) -> Unit) {
            ImageNetwork.loadImageWithView(
                view = view,
                url = url,
                placeholder = placeholder?.toUIImage(),
                setImageBlock = block
            )
        }
    }
}
