/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.utils.ThemeAttrs
import dev.icerock.moko.widgets.utils.dp
import kotlin.reflect.KClass

actual abstract class NavigationScreen actual constructor(
    private val screenFactory: ScreenFactory
) : Screen<Args.Empty>() {
    private val fragmentNavigation = FragmentNavigation(this)

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.addOnBackStackChangedListener {
            toolbar?.navigationIcon = if (childFragmentManager.backStackEntryCount > 0) {
                ThemeAttrs.getToolBarUpIndicator(requireContext())
            } else {
                null
            }
        }
        childFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    super.onFragmentStarted(fm, f)

                    updateNavigation(f)
                }
            },
            false
        )
    }

    override fun createView(context: Context, parent: ViewGroup?): View {
        val container = FrameLayout(context).apply {
            id = android.R.id.content
        }
        val toolbar = Toolbar(context).apply {
            setBackgroundColor(ThemeAttrs.getPrimaryColor(context))
            ViewCompat.setElevation(this, 4.dp(context).toFloat())
            setNavigationOnClickListener {
                childFragmentManager.popBackStack()
            }
        }

        this.toolbar = toolbar

        return LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(ThemeAttrs.getContentBackgroundColor(context))

            addView(
                toolbar,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ThemeAttrs.getToolBarHeight(context)
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

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            val instance = screenFactory.instantiateScreen(rootScreen.screenClass)
            fragmentNavigation.setScreen(instance)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ComponentActivity) {
            context.onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        fragmentManager!!.popBackStackImmediate()
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        toolbar = null
    }

    actual abstract val rootScreen: RootNavigationScreen

    actual fun <S> routeToScreen(
        screen: KClass<out S>
    ) where S : Screen<Args.Empty>, S : NavigationItem {
        val instance = screenFactory.instantiateScreen(screen)
        fragmentNavigation.routeToScreen(instance)
    }

    actual fun <A : Parcelable, S> routeToScreen(
        screen: KClass<out S>,
        args: A
    ) where S : Screen<Args.Parcel<A>>, S : NavigationItem {
        val instance = screenFactory.instantiateScreen(screen)
        instance.setArgument(args)
        fragmentNavigation.routeToScreen(instance)
    }

    actual fun <S> setScreen(screen: KClass<out S>) where S : Screen<Args.Empty>, S : NavigationItem {
        val instance = screenFactory.instantiateScreen(screen)
        fragmentNavigation.setScreen(instance)
    }

    actual fun <A : Parcelable, S> setScreen(
        screen: KClass<out S>,
        args: A
    ) where S : Screen<Args.Parcel<A>>, S : NavigationItem {
        val instance = screenFactory.instantiateScreen(screen)
        instance.setArgument(args)
        fragmentNavigation.setScreen(instance)
    }

    private fun updateNavigation(fragment: Fragment) {
        fragment as NavigationItem

        when (val navBar = fragment.navigationBar) {
            NavigationBar.None -> {
                toolbar?.visibility = View.GONE
            }
            is NavigationBar.Normal -> {
                toolbar?.visibility = View.VISIBLE
                toolbar?.title = navBar.title.toString(requireContext())
            }
        }
    }
}
