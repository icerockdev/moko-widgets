/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.widgets.core.AnyWidget
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

actual fun <T : Any> EventsDispatcher<T>.listen(
    screen: Screen<*>,
    listener: T
) {
}

actual abstract class DrawerNavigationScreen actual constructor() : Screen<Args.Empty>() {
    actual abstract val header: Any?
    actual abstract val items: List<DrawerNavigationItem>
    actual abstract val secondaryItems: List<DrawerNavigationItem>
}

actual abstract class NavigationScreen actual constructor() : Screen<Args.Empty>() {
    actual abstract val rootScreen: KClass<out Screen<Args.Empty>>
}

actual abstract class BottomNavigationScreen actual constructor() : Screen<Args.Empty>() {
    actual abstract val items: List<BottomNavigationItem>
}

actual fun <T : Parcelable> Screen<Args.Parcel<T>>.getArgument(): T {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

actual abstract class WidgetScreen<Arg : Args> actual constructor() : Screen<Arg>() {
    actual abstract fun createContentWidget(): AnyWidget
}

actual abstract class Screen<Arg : Args> : UIViewController() {
    actual inline fun <reified VM : ViewModel, Key : Any> getViewModel(
        key: Key,
        crossinline viewModelFactory: () -> VM
    ): VM {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun <T : Any> createEventsDispatcher(): EventsDispatcher<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual fun dispatchNavigation(actions: Navigation.() -> Unit) {
    }

}