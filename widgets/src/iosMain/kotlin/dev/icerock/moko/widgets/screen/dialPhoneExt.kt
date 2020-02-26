/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun Screen<*>.dialPhone(phone: String) {
    val application = UIApplication.sharedApplication
    val url = NSURL(string = "tel://${phone.filter { it in '0'..'9' }}")
    if (!application.canOpenURL(url)) {
        println("can't open url $url")
        return
    }

    application.openURL(url)
}
