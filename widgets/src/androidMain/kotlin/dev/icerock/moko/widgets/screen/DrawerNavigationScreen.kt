/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

actual abstract class DrawerNavigationScreen actual constructor(
    screenFactory: ScreenFactory
) : Screen<Args.Empty>() {
    actual abstract val header: Any?
    actual abstract val items: List<DrawerNavigationItem>

    actual var selectedItemIndex: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
}
