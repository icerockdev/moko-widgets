/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.resources.ImageResource

expect abstract class Image {
    companion object
}

expect fun Image.Companion.resource(imageResource: ImageResource): Image
expect fun Image.Companion.bitmap(bitmap: Bitmap): Image
