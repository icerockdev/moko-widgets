/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import platform.UIKit.UIViewController

actual abstract class DrawerNavigationScreen actual constructor() : Screen<Args.Empty>() {
    actual abstract val header: Any?
    actual abstract val items: List<DrawerNavigationItem>
    actual abstract val secondaryItems: List<DrawerNavigationItem>

    override fun createView(): UIViewController {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}