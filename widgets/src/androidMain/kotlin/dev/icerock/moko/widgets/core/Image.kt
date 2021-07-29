/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import android.widget.ImageView
import dev.icerock.moko.resources.ImageResource

@Suppress("UnnecessaryAbstractClass")
actual abstract class Image {
    abstract fun loadIn(imageView: ImageView)

    actual companion object
}

actual fun Image.Companion.resource(imageResource: ImageResource): Image {
    return object : Image() {
        override fun loadIn(imageView: ImageView) {
            imageView.setImageResource(imageResource.drawableResId)
        }
    }
}
