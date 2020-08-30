/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.widgets.core.style.view.WidgetSize

abstract class Widget<WS : WidgetSize> {
    abstract val size: WS

    abstract fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS>
}
