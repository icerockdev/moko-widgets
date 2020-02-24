/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import android.content.res.ColorStateList
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.graphics.colorInt
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.FragmentNavigation
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.style.state.SelectableState
import dev.icerock.moko.widgets.utils.ThemeAttrs
import dev.icerock.moko.widgets.utils.dp
import dev.icerock.moko.widgets.utils.getIntNullable

actual abstract class BottomNavigationScreen actual constructor(
    private val router: Router,
    builder: BottomNavigationItem.Builder.() -> Unit
) : Screen<Args.Empty>() {
    private val fragmentNavigation = FragmentNavigation(this)
    actual val items: List<BottomNavigationItem> =
        BottomNavigationItem.Builder().apply(builder).build()

    private var bottomNavigationView: BottomNavigationView? = null

    actual var itemStateColors: SelectableState<Color>? = null
        set(value) {
            field = value
            updateItemColors()
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

    actual var isTitleVisible: Boolean = true
        set(value) {
            field = value
            updateTitleMode()
        }

    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            childFragmentManager.popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        router.bottomNavigationScreen = this

        childFragmentManager.addOnBackStackChangedListener {
            updateBackCallbackState()
        }
        childFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    super.onFragmentStarted(fm, f)

                    val id = f.arguments?.getIntNullable(ARG_ITEM_ID_KEY) ?: return
                    if (bottomNavigationView?.selectedItemId != id) {
                        bottomNavigationView?.selectedItemId = id
                    }
                }
            },
            false
        )

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, backPressedCallback)

        updateBackCallbackState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        val context = requireContext()
        val root = FragmentContainerView(context).apply {
            id = android.R.id.content
        }
        val bottomNavigation = BottomNavigationView(context).apply {
            id = android.R.id.tabs
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

            if (item.stateIcons != null) {
                val stateListDrawable = StateListDrawable()

                ContextCompat.getDrawable(context, item.stateIcons.selected.drawableResId).also {
                    stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), it)
                }
                ContextCompat.getDrawable(context, item.stateIcons.unselected.drawableResId).also {
                    stateListDrawable.addState(intArrayOf(-android.R.attr.state_selected), it)
                }

                menuItem.icon = stateListDrawable
            }

            menuItemAction[menuItem] = {
                val instance = item.screenDesc.instantiate()
                instance.arguments = Bundle().apply { putInt(ARG_ITEM_ID_KEY, item.id) }
                fragmentNavigation.routeToScreen(instance)
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val current = childFragmentManager.findFragmentById(android.R.id.content)
            val currentItemId = current?.arguments?.getIntNullable(ARG_ITEM_ID_KEY)
            if (currentItemId != menuItem.itemId) {
                menuItemAction[menuItem]?.invoke()
            }
            true
        }

        bottomNavigationView = bottomNavigation
        updateItemColors()
        updateTitleMode()

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

        if (savedInstanceState == null && childFragmentManager.fragments.isEmpty()) {
            items.firstOrNull()?.let {
                val instance = it.screenDesc.instantiate()
                instance.arguments = Bundle().apply { putInt(ARG_ITEM_ID_KEY, it.id) }
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

    private fun updateBackCallbackState() {
        backPressedCallback.isEnabled = childFragmentManager.backStackEntryCount > 0
    }

    private fun updateItemColors() {
        val navView = bottomNavigationView ?: return
        val stateColors = itemStateColors ?: return

        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        )
        val colors = intArrayOf(
            stateColors.selected.colorInt(),
            stateColors.unselected.colorInt()
        )

        navView.itemIconTintList = ColorStateList(states, colors)
        navView.itemTextColor = ColorStateList(states, colors)
    }

    private fun updateTitleMode() {
        bottomNavigationView?.also { navView ->
            if (isTitleVisible) {
                navView.labelVisibilityMode =
                    LabelVisibilityMode.LABEL_VISIBILITY_LABELED
            } else {
                navView.labelVisibilityMode =
                    LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
            }
        }
    }

    actual class Router {
        var bottomNavigationScreen: BottomNavigationScreen? = null

        actual fun createChangeTabRoute(itemId: Int): Route<Unit> {
            return object : Route<Unit> {
                override fun route(arg: Unit) {
                    bottomNavigationScreen!!.selectedItemId = itemId
                }
            }
        }
    }

    private companion object {
        const val ARG_ITEM_ID_KEY = "itemId"
    }
}
