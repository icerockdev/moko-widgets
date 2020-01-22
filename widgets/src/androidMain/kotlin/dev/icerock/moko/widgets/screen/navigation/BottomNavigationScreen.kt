/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.FragmentNavigation
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.ScreenFactory
import dev.icerock.moko.widgets.utils.ThemeAttrs
import dev.icerock.moko.widgets.utils.dp

actual abstract class BottomNavigationScreen actual constructor(
    private val router: Router,
    builder: BottomNavigationItem.Builder.() -> Unit
) : Screen<Args.Empty>() {
    actual val items: List<BottomNavigationItem> = BottomNavigationItem.Builder().apply(builder).build()

    private val fragmentNavigation = FragmentNavigation(this)
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router.bottomNavigationScreen = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        val context = requireContext()
        val root = FrameLayout(context).apply {
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
                val instance = item.screenDesc.instantiate()
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
                root,
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
                val instance = it.screenDesc.instantiate()
                fragmentNavigation.setScreen(instance)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        bottomNavigationView = null
    }

    override fun onDestroy() {
        super.onDestroy()

        router.bottomNavigationScreen = null
    }

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

    actual class Router {
        var bottomNavigationScreen: BottomNavigationScreen? = null

        actual fun createChangeTabRoute(itemId: Int): Route<Unit> {
            return object : Route<Unit> {
                override fun route(source: Screen<*>, arg: Unit) {
                    bottomNavigationScreen!!.selectedItemId = itemId
                }
            }
        }
    }
}
