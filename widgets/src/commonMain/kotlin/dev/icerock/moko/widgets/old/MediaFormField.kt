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