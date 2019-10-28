package com.icerockdev.mpp.widgets.forms

import com.icerockdev.mpp.core.media.Bitmap

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