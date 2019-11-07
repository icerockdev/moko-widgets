/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.view

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout

// we should manually convert MarginLayoutParams to LinearLayout.LayoutParams because of
// https://android.googlesource.com/platform/frameworks/base/+/2dd20a6%5E%21/
class MarginedLinearLayout(context: Context) : LinearLayout(context) {
    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): LayoutParams {
        return when (lp) {
            is LayoutParams -> LayoutParams(lp)
            is MarginLayoutParams -> LayoutParams(lp)
            else -> LayoutParams(lp)
        }
    }
}
