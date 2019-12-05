/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import androidx.fragment.app.Fragment
import dev.icerock.moko.parcelize.Parcelable

internal class FragmentNavigation(
    private val fragment: Fragment
) {
    fun <S : Screen<Args.Empty>> routeToScreen(screen: S) {
        val fm = fragment.childFragmentManager

        fm.beginTransaction()
            .replace(android.R.id.content, screen)
            .addToBackStack(null)
            .commit()
    }

    fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: S, argument: Arg) {
        val fm = fragment.childFragmentManager

        screen.setArgument(argument)

        fm.beginTransaction()
            .replace(android.R.id.content, screen)
            .addToBackStack(null)
            .commit()
    }

    fun <S : Screen<Args.Empty>> setScreen(screen: S) {
        val fm = fragment.childFragmentManager

        fm.beginTransaction()
            .replace(android.R.id.content, screen)
            .commit()
    }
}
