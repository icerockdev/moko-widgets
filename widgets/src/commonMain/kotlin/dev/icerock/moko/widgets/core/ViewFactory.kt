/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.widgets.style.view.WidgetSize

interface ViewFactory<W : Widget<out WidgetSize>> {
    fun <WS : WidgetSize> build(
        widget: W,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS>
}