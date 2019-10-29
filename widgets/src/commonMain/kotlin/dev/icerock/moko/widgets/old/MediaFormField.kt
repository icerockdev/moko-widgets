/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.media.Bitmap

class MediaFormField(
    val path: String,
    val preview: Bitmap,
    val mediaType: Type
) {

    enum class Type {
        PHOTO,
        VIDEO
    }
}