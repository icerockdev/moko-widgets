/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.FragmentNavigation
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.TypedScreenDesc
import dev.icerock.moko.widgets.screen.unsafeSetScreenArgument
import dev.icerock.moko.widgets.style.view.FontStyle
import dev.icerock.moko.widgets.utils.ThemeAttrs
import dev.icerock.moko.widgets.utils.dp
import dev.icerock.moko.widgets.utils.sp


actual abstract class NavigationScreen<S> actual constructor(
    private val initialScreen: TypedScreenDesc<Args.Empty, S>,
    private val router: Router
) : Screen<Args.Empty>() where S : Screen<Args.Empty>, S : NavigationItem {
    private val fragmentNavigation = FragmentNavigation(this)

    private var toolbar: Toolbar? = null
    private var currentChildId: Int = 1

    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            childFragmentManager.popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            currentChildId = savedInstanceState.getInt(CURRENT_SCREEN_ID_KEY, currentChildId)
        }

        childFragmentManager.addOnBackStackChangedListener {
            updateBackCallbackState()
        }
        childFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                private val detachHandlers = mutableMapOf<Fragment, Runnable>()

                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    super.onFragmentStarted(fm, f)

                    updateNavigation(f)
                }

                override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                    super.onFragmentAttached(fm, f, context)

                    if (f is Resultable<*> && f is Screen<*>) {
                        val resultTarget = f.screenId

                        if (resultTarget != null) {
                            val target = fm.fragments
                                .filterIsInstance<Screen<*>>()
                                .firstOrNull { it.resultCode == resultTarget }

                            detachHandlers[f] = Runnable {
                                val code = f.requestCode
                                val result = f.screenResult

                                target!!.routeHandlers[code]!!.invoke(result)
                            }
                        }
                    }
                }

                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                    super.onFragmentDetached(fm, f)

                    detachHandlers.remove(f)?.run()
                }
            },
            false
        )

        router.navigationScreen = this

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
        val root = FrameLayout(context).apply {
            id = android.R.id.content
        }
        val toolbar = Toolbar(context).apply {
            ViewCompat.setElevation(this, 4.dp(context).toFloat())
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
                root,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null && childFragmentManager.fragments.isEmpty()) {
            val instance = initialScreen.instantiate()
            fragmentNavigation.setScreen(instance)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        toolbar = null
    }

    override fun onDestroy() {
        super.onDestroy()

        router.navigationScreen = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(CURRENT_SCREEN_ID_KEY, currentChildId)
    }

    private fun updateBackCallbackState() {
        backPressedCallback.isEnabled = childFragmentManager.backStackEntryCount > 0
    }

    private fun updateNavigation(fragment: Fragment) {
        fragment as NavigationItem

        val toolbar = this.toolbar ?: return
        val context = requireContext()

        when (val navBar = fragment.navigationBar) {
            NavigationBar.None -> {
                toolbar.visibility = View.GONE
            }
            is NavigationBar.Normal -> {
                toolbar.visibility = View.VISIBLE

                val title = navBar.title.toString(context)
                toolbar.title = SpannableString(title).apply {
                    val size = navBar.styles?.textStyle?.size?.toFloat()?.sp(context)
                    if (size != null) {
                        val sizeSpan = AbsoluteSizeSpan(size.toInt())
                        setSpan(sizeSpan, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                    val fontStyle = navBar.styles?.textStyle?.fontStyle
                    if (fontStyle != null) {
                        val style = when (fontStyle) {
                            FontStyle.BOLD -> Typeface.BOLD
                            FontStyle.MEDIUM -> Typeface.NORMAL
                        }
                        val styleSpan = StyleSpan(style)
                        setSpan(styleSpan, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }

                val bgColor = if (navBar.styles?.backgroundColor != null) {
                    navBar.styles.backgroundColor.argb.toInt()
                } else {
                    ThemeAttrs.getPrimaryColor(context)
                }
                toolbar.setBackgroundColor(bgColor)

                val fallbackTintColor = ThemeAttrs.getControlNormalColor(context)

                val tintColor = navBar.styles?.tintColor?.argb?.toInt() ?: fallbackTintColor

                toolbar.setTitleTextColor(tintColor)
                toolbar.overflowIcon?.also { DrawableCompat.setTint(it, tintColor) }

                if (navBar.styles?.textStyle?.color != null) {
                    toolbar.setTitleTextColor(navBar.styles.textStyle.color.argb.toInt())
                } else {
                    toolbar.setTitleTextColor(fallbackTintColor)
                }

                val backBtn = navBar.backButton
                if (backBtn != null) {
                    toolbar.navigationIcon = ContextCompat.getDrawable(context, backBtn.icon.drawableResId)
                    toolbar.setNavigationOnClickListener {
                        backBtn.action()
                    }
                } else {
                    toolbar.navigationIcon = if (childFragmentManager.backStackEntryCount > 0) {
                        ThemeAttrs.getToolBarUpIndicator(requireContext())
                    } else {
                        null
                    }
                    toolbar.setNavigationOnClickListener {
                        childFragmentManager.popBackStack()
                    }
                }
                toolbar.navigationIcon?.also { DrawableCompat.setTint(it, tintColor) }

                val actions = navBar.actions
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
        }
    }

    actual class Router {
        var navigationScreen: NavigationScreen<*>? = null

        internal actual fun <T, Arg : Args, S> createPushRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (T) -> Arg
        ): Route<T> where S : Screen<Arg>, S : NavigationItem {
            return object : Route<T> {
                override fun route(arg: T) {
                    val screen = destination.instantiate()
                    val argument = inputMapper(arg)
                    if (argument is Args.Parcel<*>) {
                        unsafeSetScreenArgument(screen, argument.args)
                    }
                    navigationScreen!!.fragmentNavigation.routeToScreen(screen)
                }
            }
        }

        internal actual fun <IT, Arg : Args, OT, R : Parcelable, S> createPushResultRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (IT) -> Arg,
            outputMapper: (R) -> OT
        ): RouteWithResult<IT, OT> where S : Screen<Arg>, S : Resultable<R>, S : NavigationItem {

            return object : RouteWithResult<IT, OT> {
                override val resultMapper: (Parcelable) -> OT = {
                    @Suppress("UNCHECKED_CAST")
                    outputMapper(it as R)
                }

                override fun route(source: Screen<*>, arg: IT, handler: RouteHandler<OT>) {
                    val navigationScreen = navigationScreen!!

                    val screen = destination.instantiate()
                    screen.requestCode = handler.requestCode
                    screen.screenId = navigationScreen.currentChildId++

                    source.resultCode = screen.screenId

                    val argument = inputMapper(arg)
                    if (argument is Args.Parcel<*>) {
                        unsafeSetScreenArgument(screen, argument.args)
                    }

                    navigationScreen.fragmentNavigation.routeToScreen(screen)
                }
            }
        }

        internal actual fun <T, Arg : Args, S> createReplaceRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (T) -> Arg
        ): Route<T> where S : Screen<Arg>, S : NavigationItem {
            return object : Route<T> {
                override fun route(arg: T) {
                    val screen = destination.instantiate()
                    val argument = inputMapper(arg)
                    if (argument is Args.Parcel<*>) {
                        unsafeSetScreenArgument(screen, argument.args)
                    }
                    navigationScreen!!.fragmentNavigation.setScreen(screen)
                }
            }
        }

        actual fun createPopRoute(): Route<Unit> {
            return object : Route<Unit> {
                override fun route(arg: Unit) {
                    navigationScreen!!.getChildFragmentManager().popBackStack()
                }
            }
        }

        actual fun createPopToRootRoute(): Route<Unit> {
            return object : Route<Unit> {
                override fun route(arg: Unit) {
                    val fragmentManager = navigationScreen!!.getChildFragmentManager()
                    for(i in 0 until fragmentManager.backStackEntryCount) {
                        fragmentManager.popBackStack()
                    }
                }
            }
        }
    }

    private companion object {
        const val CURRENT_SCREEN_ID_KEY = "navigation:screen:current_id"
    }
}
