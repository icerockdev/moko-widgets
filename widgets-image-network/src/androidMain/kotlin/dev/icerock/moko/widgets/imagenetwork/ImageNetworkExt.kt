/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.imagenetwork

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.Image

actual fun Image.Companion.network(
    url: String,
    placeholder: ImageResource?
): Image {
    return object : Image() {
        override fun loadIn(imageView: ImageView) {

            Glide.with(imageView)
                .load(Uri.parse(url))
                .apply {
                    if (placeholder != null) {
                        placeholder(placeholder.drawableResId)
                    }
                }
                .into(imageView)

        }
    }
}
