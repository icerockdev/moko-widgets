/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style

import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.utils.ThemeAttrs
import dev.icerock.moko.widgets.core.utils.dp

@Suppress("MagicNumber")
fun NavigationBar.Styles.apply(
    toolbar: Toolbar,
    context: Context
) {
    val bgColor = backgroundColor?.argb?.toInt()
        ?: ThemeAttrs.getPrimaryColor(context)

    toolbar.setBackgroundColor(bgColor)

    val fallbackTintColor = ThemeAttrs.getControlNormalColor(context)

    val tintColor = tintColor?.argb?.toInt() ?: fallbackTintColor

    toolbar.setTitleTextColor(tintColor)
    toolbar.overflowIcon?.also { DrawableCompat.setTint(it, tintColor) }

    val textColor = textStyle?.color?.argb?.toInt() ?: fallbackTintColor
    toolbar.setTitleTextColor(textColor)

    if (isShadowEnabled == false) {
        ViewCompat.setElevation(toolbar, 0f)
    } else {
        // 4 points https://material.io/design/environment/elevation.html#elevation-shadows-elevation-android
        ViewCompat.setElevation(toolbar, 4f.dp(context))
    }
}
