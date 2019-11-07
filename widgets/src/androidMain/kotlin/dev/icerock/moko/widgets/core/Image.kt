/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.resources.DrawableResource

actual abstract class Image {

    abstract fun loadIn(requestManager: RequestManager): RequestBuilder<Drawable>

    actual companion object {
        actual fun resource(drawableResource: DrawableResource): Image {
            return ResourceImage(drawableResource)
        }

        actual fun network(url: String): Image {
            return NetworkImage(url)
        }

        actual fun bitmap(bitmap: Bitmap): Image {
            return BitmapImage(bitmap)
        }
    }
}

class ResourceImage(val drawableResource: DrawableResource) : Image() {
    override fun loadIn(requestManager: RequestManager): RequestBuilder<Drawable> {
        return requestManager.load(drawableResource.drawableResId)
    }
}

class NetworkImage(val url: String) : Image() {
    override fun loadIn(requestManager: RequestManager): RequestBuilder<Drawable> {
        return requestManager.load(Uri.parse(url))
    }
}

class BitmapImage(val bitmap: Bitmap) : Image() {
    override fun loadIn(requestManager: RequestManager): RequestBuilder<Drawable> {
        return requestManager.load(bitmap.platformBitmap)
    }
}
