/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style

import android.content.Context
import android.graphics.drawable.StateListDrawable
import androidx.core.content.ContextCompat
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.style.state.PressableState

fun PressableState<ImageResource>.toStateList(context: Context) = StateListDrawable().also { selector ->
    selector.addState(
        intArrayOf(-android.R.attr.state_enabled),
        ContextCompat.getDrawable(context, disabled.drawableResId)
    )
    selector.addState(
        intArrayOf(android.R.attr.state_pressed),
        ContextCompat.getDrawable(context, pressed.drawableResId)
    )
    selector.addState(
        intArrayOf(),
        ContextCompat.getDrawable(context, normal.drawableResId)
    )
}
