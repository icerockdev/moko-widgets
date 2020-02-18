/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.app.Activity
import android.view.inputmethod.InputMethodManager

actual fun Screen<*>.hideKeyboard() {
    val activity = requireActivity()
    val focusedView = activity.currentFocus ?: return
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
}