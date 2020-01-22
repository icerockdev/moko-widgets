/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import androidx.fragment.app.Fragment

internal class FragmentNavigation(
    private val fragment: Fragment
) {
    fun <S : Screen<*>> routeToScreen(screen: S) {
        val fm = fragment.childFragmentManager

        fm.beginTransaction()
            .replace(android.R.id.content, screen)
            .addToBackStack(null)
            .commit()
    }

    fun <S : Screen<*>> setScreen(screen: S) {
        val fm = fragment.childFragmentManager

        val backStackCount = fm.backStackEntryCount
        for(i in 0 until backStackCount) {
            fm.popBackStack()
        }

        fm.beginTransaction()
            .replace(android.R.id.content, screen)
            .commit()
    }
}
