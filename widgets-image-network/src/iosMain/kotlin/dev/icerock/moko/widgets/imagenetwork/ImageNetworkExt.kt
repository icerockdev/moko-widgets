/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.imagenetwork

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.objc.dataTask
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSURLSessionConfiguration
import platform.UIKit.UIImage
import platform.UIKit.UIView

actual fun Image.Companion.network(
    url: String,
    placeholder: ImageResource?
): Image {
    return object : Image() {
        override fun apply(view: UIView, block: (UIImage?) -> Unit) {
            block(placeholder?.toUIImage())

            try {
                val tag = url.hashCode().toLong()
                view.tag = tag
                val nsUrl = NSURL.URLWithString(url) ?: return

                val config = NSURLSessionConfiguration.defaultSessionConfiguration().apply {
                    timeoutIntervalForRequest = 60.0
                }
                dataTask(
                    session = NSURLSession.sessionWithConfiguration(config),
                    url = nsUrl
                ) { data, _, error ->
                    if (error != null) {
                        println(error)
                        return@dataTask
                    }
                    if (data == null) return@dataTask

                    if (view.tag != tag) return@dataTask

                    block(UIImage.imageWithData(data))
                }?.resume()
            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }
}
