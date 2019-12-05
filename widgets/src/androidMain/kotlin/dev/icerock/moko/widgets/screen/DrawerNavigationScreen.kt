/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.content.Context
import android.view.ViewGroup
import dev.icerock.moko.widgets.core.View

actual abstract class DrawerNavigationScreen actual constructor(
    screenFactory: ScreenFactory
) : Screen<Args.Empty>() {
    override fun createView(context: Context, parent: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    actual abstract val header: Any?
    actual abstract val items: List<DrawerNavigationItem>

    actual var selectedItemIndex: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
}
