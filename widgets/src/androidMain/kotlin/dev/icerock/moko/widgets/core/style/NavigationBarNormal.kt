/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentManager
import dev.icerock.moko.widgets.core.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.core.style.view.FontStyle
import dev.icerock.moko.widgets.core.utils.ThemeAttrs
import dev.icerock.moko.widgets.core.utils.sp

fun NavigationBar.Normal.apply(
    toolbar: Toolbar,
    context: Context,
    fragmentManager: FragmentManager
) {
    toolbar.visibility = View.VISIBLE

    styles?.apply(toolbar, context)

    val title = title.toString(context)
    toolbar.title = SpannableString(title).apply {
        val size = styles?.textStyle?.size?.toFloat()?.sp(context)
        if (size != null) {
            val sizeSpan = AbsoluteSizeSpan(size.toInt())
            setSpan(sizeSpan, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val fontStyle = styles?.textStyle?.fontStyle
        if (fontStyle != null) {
            val style = when (fontStyle) {
                FontStyle.BOLD -> Typeface.BOLD
                FontStyle.MEDIUM -> Typeface.NORMAL
                FontStyle.ITALIC -> Typeface.ITALIC
            }
            val styleSpan = StyleSpan(style)
            setSpan(styleSpan, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    val fallbackTintColor = ThemeAttrs.getControlNormalColor(context)
    val tintColor = styles?.tintColor?.argb?.toInt() ?: fallbackTintColor

    val backBtn = backButton
    if (backBtn != null) {
        toolbar.navigationIcon = ContextCompat.getDrawable(context, backBtn.icon.drawableResId)
        toolbar.setNavigationOnClickListener {
            backBtn.action()
        }
    } else {
        toolbar.navigationIcon = if (fragmentManager.backStackEntryCount > 0) {
            ThemeAttrs.getToolBarUpIndicator(context)
        } else {
            null
        }
        toolbar.setNavigationOnClickListener {
            fragmentManager.popBackStack()
        }
    }
    toolbar.navigationIcon?.also { DrawableCompat.setTint(it, tintColor) }

    val actions = actions
    if (actions != null) {
        actions.forEach { barBtn ->
            val item = toolbar.menu.add("$barBtn")
            item.icon = ContextCompat.getDrawable(context, barBtn.icon.drawableResId)
            DrawableCompat.setTint(item.icon, tintColor)
            item.setOnMenuItemClickListener {
                barBtn.action()
                true
            }
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
    } else {
        toolbar.menu.clear()
    }
}
