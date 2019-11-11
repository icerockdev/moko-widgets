/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.core.Widget
import platform.UIKit.UIViewController

actual abstract class WidgetScreen<Arg : Args> actual constructor() : Screen<Arg>() {
    actual abstract fun createContentWidget(): Widget

    override fun createView(): UIViewController {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}