/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import platform.UIKit.UIColor
import platform.UIKit.UIDevice
import platform.UIKit.systemBackgroundColor

val UIColor.Companion.safeSystemBackgroundColor: UIColor
    get() {
        return if (UIDevice.currentDevice.systemVersion.compareTo("13.0") < 0) {
            UIColor.whiteColor
        } else {
            UIColor.systemBackgroundColor
        }
    }