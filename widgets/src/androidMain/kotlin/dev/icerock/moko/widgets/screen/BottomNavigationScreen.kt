/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.utils.ThemeAttrs
import dev.icerock.moko.widgets.utils.dp

actual abstract class BottomNavigationScreen actual constructor(
    private val screenFactory: ScreenFactory
) : Screen<Args.Empty>() {
    private val fragmentNavigation = FragmentNavigation(this)
    private var bottomNavigationView: BottomNavigationView? = null

    override fun createView(context: Context, parent: ViewGroup?): View {
        val container = FrameLayout(context).apply {
            id = android.R.id.content
        }
        val bottomNavigation = BottomNavigationView(context).apply {
            ViewCompat.setElevation(this, 8.dp(context).toFloat())
            val color = bottomNavigationColor
            if (color != null) {
                setBackgroundColor(color.argb.toInt())
            } else {
                setBackgroundResource(android.R.color.white)
            }
        }

        val menuItemAction = mutableMapOf<MenuItem, () -> Unit>()

        items.forEach { item ->
            val menuItem = bottomNavigation.menu.add(
                Menu.NONE, item.id, Menu.NONE, item.title.toString(context)
            )
            item.icon?.also { menuItem.setIcon(it.drawableResId) }

            menuItemAction[menuItem] = {
                val instance = screenFactory.instantiateScreen(item.screen)
                fragmentNavigation.routeToScreen(instance)
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            menuItemAction[menuItem]?.invoke()
            true
        }

        bottomNavigationView = bottomNavigation

        return LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            clipChildren = false
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(ThemeAttrs.getContentBackgroundColor(context))

            addView(
                container,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
            )
            addView(
                bottomNavigation,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            items.firstOrNull()?.let {
                val instance = screenFactory.instantiateScreen(it.screen)
                fragmentNavigation.setScreen(instance)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        bottomNavigationView = null
    }

    actual abstract val items: List<BottomNavigationItem>

    actual var selectedItemId: Int
        get() = bottomNavigationView?.selectedItemId ?: -1
        set(value) {
            bottomNavigationView?.selectedItemId = value
        }

    actual var bottomNavigationColor: Color? = null
        set(value) {
            field = value
            bottomNavigationView?.also { navView ->
                if (value == null) {
                    navView.setBackgroundResource(android.R.color.white)
                } else {
                    navView.setBackgroundColor(value.argb.toInt())
                }
            }
        }
}
