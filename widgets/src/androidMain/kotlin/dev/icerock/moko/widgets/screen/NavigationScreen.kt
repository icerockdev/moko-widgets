/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.widgets.core.View
import kotlin.reflect.KClass

actual abstract class NavigationScreen actual constructor() : Screen<Args.Empty>(),
    Navigation {
    private val fragmentNavigation = FragmentNavigation(this)

    override fun createView(context: Context, parent: ViewGroup?): View {
        val container = FrameLayout(context).apply {
            id = android.R.id.content
        }
        val toolbar = Toolbar(context).apply {
            title = "My Toolbar"
        }

        return LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL

            addView(
                toolbar,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getToolBarHeight(context)
                )
            )
            addView(
                container,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    private fun getToolBarHeight(context: Context): Int {
        val attrs = intArrayOf(android.R.attr.actionBarSize)
        val ta = context.obtainStyledAttributes(attrs)
        val toolBarHeight = ta.getDimensionPixelSize(0, -1)
        ta.recycle()
        return toolBarHeight
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentNavigation.setScreen(rootScreen)
    }

    actual abstract val rootScreen: KClass<out Screen<Args.Empty>>

    override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
        fragmentNavigation.routeToScreen(screen)
    }

    override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
        fragmentNavigation.routeToScreen(screen, argument)
    }
}