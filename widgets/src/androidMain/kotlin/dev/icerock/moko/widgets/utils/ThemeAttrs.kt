/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.AttrRes
import dev.icerock.moko.widgets.R

object ThemeAttrs {
    fun getContentBackgroundColor(context: Context): Int {
        return getAttribute(context, android.R.attr.colorBackground) {
            getColor(0, -1)
        }
    }

    fun getPrimaryColor(context: Context): Int {
        return getAttribute(context, R.attr.colorPrimary) {
            getColor(0, -1)
        }
    }

    fun getControlNormalColor(context: Context): Int {
        return getAttribute(context, R.attr.colorControlNormal) {
            getColor(0, -1)
        }
    }

    fun getPrimaryDarkColor(context: Context): Int {
        return getAttribute(context, R.attr.colorPrimaryDark) {
            getColor(0, -1)
        }
    }

    fun getLightStatusBar(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false

        return getAttribute(context, android.R.attr.windowLightStatusBar) {
            getBoolean(0, false)
        }
    }

    fun getToolBarHeight(context: Context): Int {
        return getAttribute(context, android.R.attr.actionBarSize) {
            getDimensionPixelSize(0, -1)
        }
    }

    fun getToolBarUpIndicator(context: Context): Drawable {
        return getAttribute(context, android.R.attr.homeAsUpIndicator) {
            getDrawable(0)
        }
    }

    fun getSelectedTabColor(context: Context): Int {
        return getAttribute(context, R.attr.tabTextAppearance) {
            val resId = getResourceId(0, R.style.TextAppearance_Design_Tab)

            val stateList = getAttribute(context, resId) {
                getColorStateList(androidx.appcompat.R.styleable.TextAppearance_android_textColor)
            }

            stateList.getColorForState(intArrayOf(android.R.attr.state_selected), stateList.defaultColor)
        }
    }

    fun getNormalTabColor(context: Context): Int {
        return getAttribute(context, R.attr.tabTextAppearance) {
            val resId = getResourceId(0, R.style.TextAppearance_Design_Tab)

            val stateList = getAttribute(context, resId) {
                getColorStateList(androidx.appcompat.R.styleable.TextAppearance_android_textColor)
            }

            stateList.getColorForState(intArrayOf(), stateList.defaultColor)
        }
    }

    private fun <T> getAttribute(context: Context, @AttrRes id: Int, getter: TypedArray.() -> T?): T {
        val attrs = intArrayOf(id)
        val ta = context.obtainStyledAttributes(attrs)
        val result = ta.getter()
        ta.recycle()
        return result!!
    }
}
