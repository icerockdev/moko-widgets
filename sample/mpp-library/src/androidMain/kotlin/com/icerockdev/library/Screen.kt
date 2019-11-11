/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.core.ViewFactoryContext
import java.util.concurrent.Executor
import kotlin.reflect.KClass

actual abstract class Screen<Arg : Args> : Fragment() {
    private val navigationQueue: MutableList<Navigation.() -> Unit> = mutableListOf()

    actual inline fun <reified VM : ViewModel, Key : Any> getViewModel(
        key: Key,
        crossinline viewModelFactory: () -> VM
    ): VM {
        return ViewModelProvider(this, createViewModelFactory { viewModelFactory() })
            .get(key.toString(), VM::class.java)
    }

    actual fun <T : Any> createEventsDispatcher(): EventsDispatcher<T> {
        val mainLooper = Looper.getMainLooper()
        val mainHandler = Handler(mainLooper)
        val mainExecutor = Executor { mainHandler.post(it) }
        return EventsDispatcher(mainExecutor)
    }

    actual fun dispatchNavigation(actions: Navigation.() -> Unit) {
        val navigation = getNavigation()
        if (navigation != null) {
            navigation.actions()
        } else {
            navigationQueue.add(actions)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (navigationQueue.isNotEmpty()) {
            val navigation = getNavigation()
            if (navigation != null) {
                navigationQueue.forEach { action ->
                    navigation.action()
                }
                navigationQueue.clear()
            }
        }
    }

    private fun getNavigation(): Navigation? {
        return if (this is Navigation) {
            this
        } else {
            (parentFragment as? Screen<*>)?.getNavigation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        return createView(context = requireContext(), parent = container)
    }

    abstract fun createView(context: Context, parent: ViewGroup?): View

    fun onBackPressed(): Boolean {
        val rootScreen = childFragmentManager.findFragmentById(android.R.id.content)
        if (rootScreen is Screen<*>) {
            if (rootScreen.onBackPressed()) return true
        }

        if (fragmentManager!!.popBackStackImmediate()) return true
        return false
    }
}

private const val sScreenArgsParameter = "args"

actual fun <T : Parcelable> Screen<Args.Parcel<T>>.getArgument(): T {
    return requireNotNull(arguments?.getParcelable(sScreenArgsParameter)) { "screen with arguments should have notnull args" }
}

private fun <T : Parcelable> Screen<Args.Parcel<T>>.setArgument(arg: T) {
    arguments = Bundle().apply {
        putParcelable(sScreenArgsParameter, arg)
    }
}

actual abstract class WidgetScreen<Arg : Args> actual constructor() : Screen<Arg>() {
    actual abstract fun createContentWidget(): AnyWidget

    override fun createView(context: Context, parent: ViewGroup?): View {
        val widget = createContentWidget()
        return widget.buildView(
            ViewFactoryContext(
                context = context,
                lifecycleOwner = this,
                parent = parent
            )
        )
    }
}

private class FragmentNavigation(private val fragment: Fragment) : Navigation {
    override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
        val fm = fragment.childFragmentManager ?: return

        val instance = screen.java.newInstance()
        fm.beginTransaction()
            .replace(android.R.id.content, instance)
            .addToBackStack(null)
            .commit()
    }

    override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
        val fm = fragment.childFragmentManager ?: return

        val instance = screen.java.newInstance()
        instance.setArgument(argument)

        fm.beginTransaction()
            .replace(android.R.id.content, instance)
            .addToBackStack(null)
            .commit()
    }

    fun <S : Screen<Args.Empty>> setScreen(screen: KClass<S>) {
        val fm = fragment.childFragmentManager ?: return

        val instance = screen.java.newInstance()
        fm.beginTransaction()
            .replace(android.R.id.content, instance)
            .commit()
    }
}

actual abstract class BottomNavigationScreen actual constructor() : Screen<Args.Empty>(), Navigation {
    private val fragmentNavigation = FragmentNavigation(this)

    override fun createView(context: Context, parent: ViewGroup?): View {
        val container = FrameLayout(context).apply {
            id = android.R.id.content
        }
        val bottomNavigation = BottomNavigationView(context)

        val menuItemAction = mutableMapOf<MenuItem, () -> Unit>()

        items.forEach { item ->
            val menuItem = bottomNavigation.menu.add(
                Menu.NONE, item.id, Menu.NONE, item.title.toString(context)
            )
            item.icon?.also { menuItem.setIcon(it.drawableResId) }

            menuItemAction[menuItem] = {
                routeToScreen(item.screen)
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            menuItemAction[menuItem]?.invoke()
            true
        }

        return LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL

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
                fragmentNavigation.setScreen(it.screen)
            }
        }
    }

    actual abstract val items: List<BottomNavigationItem>

    override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
        fragmentNavigation.routeToScreen(screen)
    }

    override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
        fragmentNavigation.routeToScreen(screen, argument)
    }
}

actual abstract class NavigationScreen actual constructor() : Screen<Args.Empty>(), Navigation {
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
                    context.resources.getDimensionPixelSize(R.dimen.mtrl_toolbar_default_height)
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

actual abstract class DrawerNavigationScreen actual constructor() : Screen<Args.Empty>(), Navigation {
    override fun createView(context: Context, parent: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual abstract val header: Any?
    actual abstract val items: List<DrawerNavigationItem>
    actual abstract val secondaryItems: List<DrawerNavigationItem>

    private val fragmentNavigation = FragmentNavigation(this)

    override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
        fragmentNavigation.routeToScreen(screen)
    }

    override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
        fragmentNavigation.routeToScreen(screen, argument)
    }
}

actual fun <T : Any> EventsDispatcher<T>.listen(
    screen: Screen<*>,
    listener: T
) {
    bind(screen, listener)
}