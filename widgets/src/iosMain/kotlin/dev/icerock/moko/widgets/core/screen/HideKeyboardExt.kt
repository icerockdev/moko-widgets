/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import platform.UIKit.endEditing

actual fun Screen<*>.hideKeyboard() {
    viewController.view.endEditing(true)
}
