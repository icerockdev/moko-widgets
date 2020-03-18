/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.resources.ImageResource

expect abstract class Image {

    companion object {
        fun resource(imageResource: ImageResource): Image
        fun network(url: String, placeholder: ImageResource? = null): Image
        fun bitmap(bitmap: Bitmap): Image
    }
}
