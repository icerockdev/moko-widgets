/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.resources.ImageResource

actual abstract class Image {

    abstract fun loadIn(imageView: ImageView)

    actual companion object {
        actual fun resource(imageResource: ImageResource): Image {
            return ResourceImage(imageResource)
        }

        actual fun network(url: String, placeholder: ImageResource?): Image {
            return NetworkImage(url, placeholder)
        }

        actual fun bitmap(bitmap: Bitmap): Image {
            return BitmapImage(bitmap)
        }
    }
}

private class ResourceImage(val imageResource: ImageResource) : Image() {
    override fun loadIn(imageView: ImageView) {
        imageView.setImageResource(imageResource.drawableResId)
    }
}

private class NetworkImage(val url: String, val placeholder: ImageResource?) : Image() {
    override fun loadIn(imageView: ImageView) {

        Glide.with(imageView)
            .load(Uri.parse(url))
            .apply {
                if(placeholder != null) {
                    placeholder(placeholder.drawableResId)
                }
            }
            .into(imageView)

    }
}

private class BitmapImage(val bitmap: Bitmap) : Image() {
    override fun loadIn(imageView: ImageView) {
        imageView.setImageBitmap(bitmap.platformBitmap)
    }
}
