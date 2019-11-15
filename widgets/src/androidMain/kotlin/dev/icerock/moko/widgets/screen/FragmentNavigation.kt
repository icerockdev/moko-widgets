/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import androidx.fragment.app.Fragment
import dev.icerock.moko.parcelize.Parcelable
import kotlin.reflect.KClass

internal class FragmentNavigation(
    private val fragment: Fragment,
    private val screenFactory: ScreenFactory
) :
    Navigation {
    override fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>) {
        val fm = fragment.childFragmentManager ?: return

        val instance = screenFactory.instantiateScreen(screen)
        fm.beginTransaction()
            .replace(android.R.id.content, instance)
            .addToBackStack(null)
            .commit()
    }

    override fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg) {
        val fm = fragment.childFragmentManager ?: return

        val instance = screenFactory.instantiateScreen(screen)
        instance.setArgument(argument)

        fm.beginTransaction()
            .replace(android.R.id.content, instance)
            .addToBackStack(null)
            .commit()
    }

    fun <S : Screen<Args.Empty>> setScreen(screen: KClass<S>) {
        val fm = fragment.childFragmentManager ?: return

        val instance = screenFactory.instantiateScreen(screen)
        fm.beginTransaction()
            .replace(android.R.id.content, instance)
            .commit()
    }
}