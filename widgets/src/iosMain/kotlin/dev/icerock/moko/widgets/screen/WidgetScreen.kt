/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import platform.UIKit.UIViewController

actual abstract class WidgetScreen<Arg : Args> actual constructor() : Screen<Arg>() {
    actual abstract fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>

    override fun createViewController(): UIViewController {
        return WidgetViewController().apply {
            widget = createContentWidget()
            screen = this@WidgetScreen
        }
    }
}
