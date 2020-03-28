/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.imagenetwork

import cocoapods.SDWebImage.sd_internalSetImageWithURL
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.Image
import platform.Foundation.NSURL
import platform.UIKit.UIImage
import platform.UIKit.UIView

actual fun Image.Companion.network(
    url: String,
    placeholder: ImageResource?
): Image {
    return object : Image() {
        override fun apply(view: UIView, block: (UIImage?) -> Unit) {
            val tag = url.hashCode().toLong()
            view.tag = tag

            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl == null) {
                println("url \"$url\"")
                return
            }

            view.sd_internalSetImageWithURL(
                url = nsUrl,
                context = null,
                placeholderImage = placeholder?.toUIImage(),
                progress = null,
                completed = null,
                options = 0UL,
                setImageBlock = { image, _, _, _ ->
                    if (view.tag != tag) return@sd_internalSetImageWithURL // view reused

                    block(image)
                }
            )
        }
    }
}
