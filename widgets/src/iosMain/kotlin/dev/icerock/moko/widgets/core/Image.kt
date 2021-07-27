/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.resources.ImageResource
import platform.UIKit.UIImage
import platform.UIKit.UIView

@Suppress("UnnecessaryAbstractClass")
actual abstract class Image {
    abstract fun apply(view: UIView, block: (UIImage?) -> Unit)

    actual companion object
}

actual fun Image.Companion.resource(imageResource: ImageResource): Image {
    return object : Image() {
        override fun apply(view: UIView, block: (UIImage?) -> Unit) {
            val image = requireNotNull(imageResource.toUIImage()) { "resource not found" }
            block(image)
        }
    }
}
