/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Context
import android.view.ViewGroup
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.widgets.core.View
import kotlin.reflect.KClass

actual abstract class DrawerNavigationScreen actual constructor() : Screen<Args.Empty>(),
    Navigation {
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