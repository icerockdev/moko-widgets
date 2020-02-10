/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import android.content.Context
import android.graphics.drawable.Drawable
import dev.icerock.moko.widgets.R

object ThemeAttrs {
    fun getContentBackgroundColor(context: Context): Int {
        val attrs = intArrayOf(android.R.attr.colorBackground)
        val ta = context.obtainStyledAttributes(attrs)
        val result = ta.getColor(0, -1)
        ta.recycle()
        return result
    }

    fun getPrimaryColor(context: Context): Int {
        val attrs = intArrayOf(R.attr.colorPrimary)
        val ta = context.obtainStyledAttributes(attrs)
        val result = ta.getColor(0, -1)
        ta.recycle()
        return result
    }

    fun getControlNormalColor(context: Context): Int {
        val attrs = intArrayOf(R.attr.colorControlNormal)
        val ta = context.obtainStyledAttributes(attrs)
        val result = ta.getColor(0, -1)
        ta.recycle()
        return result
    }

    fun getPrimaryDarkColor(context: Context): Int {
        val attrs = intArrayOf(R.attr.colorPrimaryDark)
        val ta = context.obtainStyledAttributes(attrs)
        val result = ta.getColor(0, -1)
        ta.recycle()
        return result
    }

    fun getToolBarHeight(context: Context): Int {
        val attrs = intArrayOf(android.R.attr.actionBarSize)
        val ta = context.obtainStyledAttributes(attrs)
        val toolBarHeight = ta.getDimensionPixelSize(0, -1)
        ta.recycle()
        return toolBarHeight
    }

    fun getToolBarUpIndicator(context: Context): Drawable {
        val attrs = intArrayOf(android.R.attr.homeAsUpIndicator)
        val ta = context.obtainStyledAttributes(attrs)
        val indicator = ta.getDrawable(0)
        ta.recycle()
        return indicator!!
    }
}
