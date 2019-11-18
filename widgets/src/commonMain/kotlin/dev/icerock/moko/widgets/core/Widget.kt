/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.widgets.style.view.WidgetSize

abstract class Widget {
    abstract fun buildView(viewFactoryContext: ViewFactoryContext): View

    abstract val layoutParams: LayoutParams

    interface Style

    interface LayoutParams {
        val size: WidgetSize
    }
}
