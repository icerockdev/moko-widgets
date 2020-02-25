/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentManager
import dev.icerock.moko.widgets.R
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.style.view.FontStyle
import dev.icerock.moko.widgets.utils.ThemeAttrs
import dev.icerock.moko.widgets.utils.sp


fun NavigationBar.Search.apply(
    toolbar: Toolbar,
    context: Context,
    fragmentManager: FragmentManager
) {
    toolbar.visibility = View.VISIBLE

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
            }
            val styleSpan = StyleSpan(style)
            setSpan(styleSpan, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    val bgColor = styles?.backgroundColor?.argb?.toInt()
        ?: ThemeAttrs.getPrimaryColor(context)

    toolbar.setBackgroundColor(bgColor)

    val fallbackTintColor = ThemeAttrs.getControlNormalColor(context)

    val tintColor = styles?.tintColor?.argb?.toInt() ?: fallbackTintColor

    toolbar.setTitleTextColor(tintColor)
    toolbar.overflowIcon?.also { DrawableCompat.setTint(it, tintColor) }

    val textColor = styles?.textStyle?.color?.argb?.toInt() ?: fallbackTintColor
    toolbar.setTitleTextColor(textColor)

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

    // TODO SEARCH
    val searchView = SearchView(context).apply {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQuery.value = query
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchQuery.value = newText
                return true
            }

        })
        setOnSearchClickListener { _ ->
            searchQuery.value = query.toString()
        }
        queryHint = searchPlaceholder?.toString(context)

        applyBackgroundIfNeeded(androidSearchBackground)

        val mlp = findViewById<View>(R.id.search_edit_frame).layoutParams as? ViewGroup.MarginLayoutParams
        mlp?.leftMargin = 0
        mlp?.rightMargin = 0
    }

    toolbar.menu.clear()
    val searchItem = toolbar.menu.add("search")
    searchItem.icon = searchView.findViewById<ImageView>(R.id.search_button).drawable
    searchItem.actionView = searchView
    searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_ALWAYS)
}
