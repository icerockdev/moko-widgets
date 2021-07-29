/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDefault
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController

fun UIViewController.getStatusBarStyle(isLightStatusBar: Boolean?): UIStatusBarStyle? {
    if (isLightStatusBar == null) return null

    // light status bar so content should be inverse
    return if (!isLightStatusBar) {
        UIStatusBarStyleLightContent
    } else {
        UIStatusBarStyleDefault
    }
}
